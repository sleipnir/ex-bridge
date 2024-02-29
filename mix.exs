defmodule ExBridge.MixProject do
  use Mix.Project

  def project do
    [
      app: :ex_bridge,
      version: "0.1.0",
      elixir: "~> 1.15",
      start_permanent: Mix.env() == :prod,
      deps: deps()
    ]
  end

  # Run "mix help compile.app" to learn about applications.
  def application do
    [
      extra_applications: [:logger],
      mod: {ExBridge.Application, []}
    ]
  end

  # Run "mix help deps" to learn about dependencies.
  defp deps do
    [
      {:protobuf, "~> 0.11"},
      {:protobuf_generate, "~> 0.1"},
      {:uuid, "~> 1.1"}
    ]
  end
end
