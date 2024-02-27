# Variables
VERSION := 1.0-SNAPSHOT
PACKAGE := target/spawn-java-demo-${VERSION}-shaded.jar
BASE_PATH := $(shell dirname $(realpath $(firstword $(MAKEFILE_LIST))))

clean:
	mix deps.clean --all

build: clean
	mix compile

compile-protos:
	mix protobuf.generate \
	--include-path=${BASE_PATH}/priv/protos \
	--include-path=${BASE_PATH}/priv/protos/google/protobuf \
	--include-path=${BASE_PATH}/priv/protos/google/api \
	--include-path=${BASE_PATH}/priv/protos/io/cloudevents/v1 \
	--include-path=${BASE_PATH}/priv/protos/eigr/functions/protocol/actors \
	--generate-descriptors=true \
	--output-path=./lib/ex_bridge/types google/api/annotations.proto google/api/http.proto google/protobuf/any.proto io/cloudevents/v1/spec.proto eigr/functions/protocol/actors/actor.proto eigr/functions/protocol/actors/extensions.proto eigr/functions/protocol/actors/protocol.proto eigr/functions/protocol/actors/state.proto
