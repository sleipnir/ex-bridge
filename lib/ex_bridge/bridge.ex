defmodule ExBridge.Bridge do
  use GenServer

  @impl true
  def ini({callback, init_opts, port_args}) do
    Process.flag(:trap_exit, true)

    port = open(init_opts, port_args)

    {:ok, %{}}
  end

  defp port(init_opts, port_args) do
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
end
