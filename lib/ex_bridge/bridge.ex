defmodule ExBridge.Bridge do
  @moduledoc """
  TODO
  """
  use GenServer
  require Logger

  @spec port :: port | nil
  def port, do: Process.get({__MODULE__, :port})

  @spec command(port, String.t(), any(), pid()) :: :ok
  def command(port, name, args \\ [], caller \\ nil) do
    data = serialize(name, args, caller)
    Port.command(port, data)
    :ok
  end

  defp serialize(name, args, caller) when not is_nil(caller) and is_pid(caller) do
    ref =
      :erlang.pid_to_list(caller)
      |> to_string()

    nil
  end

  @impl GenServer
  def init({callback, init_opts, port_args}) do
    Process.flag(:trap_exit, true)

    port = open(init_opts, port_args)
    Process.put({__MODULE__, :port}, port)

    {:ok, %{}}
  end

  @impl GenServer
  def terminate(reason, state) do
    if port() != nil, do: close()
  end

  @impl GenServer
  def handle_info({port, {:exit_status, status}}, state) when is_port(port) do
    Logger.error("Unexpected port exit with status #{status}")
    Process.delete({__MODULE__, :port})
    {:stop, :port_crash, state}
  end

  @impl GenServer
  def handle_info({port, {:data, msg}}, state) when is_port(port) do
    {:noreply, state}
  end

  defp open(init_opts, port_args) do
    Port.open(
      {:spawn_executable, System.find_executable(init_opts.executable)},
      [
        :nouse_stdio,
        :binary,
        :exit_status,
        packet: 4,
        args: init_opts.arguments
      ]
    )
  end

  defp close do
    port = port()
    # command(port, :stop)

    receive do
      {^port, {:exit_status, 0}} -> :ok
      {^port, {:exit_status, status}} -> {:error, status}
    after
      :timer.seconds(5) -> {:error, :timeout}
    end
  end
end
