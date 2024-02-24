defmodule ExMultilangBridgeTest do
  use ExUnit.Case
  doctest ExMultilangBridge

  test "greets the world" do
    assert ExMultilangBridge.hello() == :world
  end
end
