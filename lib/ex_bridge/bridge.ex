defmodule ExBridge.Bridge do
  @moduledoc """
  A GenServer module representing a bridge between Elixir and a Java process.

  The module provides functions to start the bridge, send synchronous and asynchronous messages to Java,
  and handles communication between Elixir and Java using file descriptors.
  """

  use GenServer
  require Logger

  alias Io.Cloudevents.V1.CloudEvent

  @doc """
  Manifest struct representing the configuration of the Java process.

  ## Examples

      %ExBridge.Bridge.Manifest{
        executable: "/path/to/java/executable",
        arguments: ["arg1", "arg2"]
      }
  """
  defmodule Manifest do
    defstruct executable: nil, arguments: []

    @type t :: %__MODULE__{
            executable: String.t(),
            arguments: list(String.t())
          }
  end

  @doc """
  Starts the bridge GenServer with the provided manifest configuration.

  ## Examples

      ExBridge.Bridge.start_link(%ExBridge.Bridge.Manifest{
        executable: "/path/to/java/executable",
        arguments: ["arg1", "arg2"]
      })
  """
  def start_link(manifest) do
    GenServer.start_link(__MODULE__, manifest, name: __MODULE__)
  end

  @doc """
  Sends a synchronous message to the Java process through the bridge.
  This function blocks until a response is received from Java.

  ## Examples

      ExBridge.Bridge.send_sync_message(pid, message)
  """
  def send_sync_message(message) do
    GenServer.call(__MODULE__, {:send_sync_message, message})
  end

  @doc """
  Sends an asynchronous message to the Java process through the bridge.
  This function does not wait for a response from Java.

  ## Examples

      ExBridge.Bridge.send_async_message(pid, message)
  """
  def send_async_message(message) do
    GenServer.cast(__MODULE__, {:send_async_message, message})
  end

  defp serialize(name, payload, caller) when not is_nil(caller) and is_pid(caller) do
    ref =
      :erlang.pid_to_list(caller)
      |> to_string()
      |> Base.encode32()

    event = %CloudEvent{
      id: UUID.uuid1(),
      source: "/spawn/caller/pid/#{ref}",
      spec_version: "1.0",
      type: name,
      data: {:binary_data, payload}
    }

    CloudEvent.encode(event)
  end

  @impl GenServer
  def init(%Manifest{} = manifest) do
    Process.flag(:trap_exit, true)

    {:ok, port} = open(manifest)
    Logger.info("Port opened: #{inspect(Port.info(port))}")

    # Use an atom instead of a tuple for Process.put
    Process.put(:port, port)

    {:ok, %{port: port}}
  end

  @doc """
  Handles an asynchronous message by serializing and sending it to Java.
  This function does not wait for a response from Java.
  """
  def handle_cast({:send_async_message, message}, state) do
    do_send(message, state, :noreply)
  end

  @doc """
  Handles a synchronous message by serializing, sending it to Java, and waiting for a response.
  """
  def handle_call({:send_sync_message, message}, _from, state) do
    do_send(message, state, :reply)
  end

  @doc """
  Handles a synchronous or asynchronous message by serializing, sending to Java, and optionally waiting for a response.
  """
  defp do_send(message, state, reply_mode) do
    # Serialize the message to binary
    binary_message = CloudEvent.encode(message)

    # Send the length of the message followed by the message itself to Java
    send_data(state.port, <<byte_size(binary_message)>>)
    send_data(state.port, binary_message)

    if reply_mode == :reply do
      {:reply, :ok, state}
    else
      {:noreply, state}
    end
  end

  defp handle_response(response) do
    # Implement logic to handle the response from Java
    Logger.info("Received response from Java: #{inspect(response)}")
  end

  @doc """
  Handles a data message from Java, receives the response length and message,
  decodes and handles the response.
  """
  def handle_info({port, {:data, msg}}, state) do
    handle_java_message(msg, state)
  end

  def handle_info({:EXIT, port, reason}, state) do
    Logger.debug("Exiting program with reason #{inspect(reason)} for Port #{inspect(port)}")
    {:noreply, state}
  end

  defp handle_java_message(msg, state) do
    Logger.debug("Received message #{inspect(msg)}")

    with decoded_message <- CloudEvent.decode(msg) do
      handle_java_response(decoded_message, state)
    else
      error ->
        # Handle errors or unexpected conditions here
        Logger.error("Error during parse of received message. Details: #{inspect(error)}")
        {:noreply, state}
    end

    {:noreply, state}
  end

  defp handle_java_response(decoded_message, state) do
    Logger.info("Received response from client: #{inspect(decoded_message)}")
    response = CloudEvent.encode(decoded_message)
    send_data(state.port, <<byte_size(response)>>)
    send_data(state.port, response)
  end

  defp send_data(port, data) do
    send(port, {self(), {:command, data}})
  end

  @impl GenServer
  def terminate(reason, state) do
    Logger.info("Terminating with reason #{inspect(reason)}...")
    {:ok, state}
  end

  defp open(manifest) do
    port =
      Port.open(
        {:spawn_executable, System.find_executable(manifest.executable)},
        [
          # :stream,
          :binary,
          # :stderr_to_stdout,
          :exit_status,
          :nouse_stdio,
          args: manifest.arguments
        ]
      )

    {:ok, port}
  end
end
