rm -fr ../data_impl/src/main/java/com/serenity/data_impl/realm/model/*
files=`find ../../protobuf -name \*.proto -print`
protoc --plugin=protoc-gen-run=run.sh --proto_path=../../protobuf/ --run_out=../data_impl/src/main/java/com/serenity/data_impl/realm/model/ $files
