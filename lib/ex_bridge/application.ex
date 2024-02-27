defmodule ExBridge.Application do
  @moduledoc false
  use Application

  @impl true
  def start(_type, _args) do
    children = [
      {
        ExBridge.Bridge,
        %ExBridge.Bridge.Manifest{
          executable: "java",
          arguments: [
            "-cp",
            "#{Application.app_dir(:ex_bridge)}/priv/drivers/java-port-0.1.0.jar",
            "io.eigr.ports.java.Main"
          ]
        }
      }
    ]

    opts = [strategy: :one_for_one, name: ExBridge.Supervisor]
    Supervisor.start_link(children, opts)
  end
end
