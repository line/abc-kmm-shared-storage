#! /bin/bash
current_dir=$1
target_file_dir="$1/cinterop/keychain/"
target_file_name="Keychain.def"

rm -f "$target_file_dir$target_file_name"

echo "writing config to $target_file_dir$target_file_name"
echo "currentdir $current_dir"
mkdir -p ".$target_file_dir"

echo "->mkdir .$target_file_dir"

echo \
"language = Objective-C
headers = headers/Keychain.h headers/KeychainQuery.h
compilerOpts = -F/cinterop/keychain
linkerOpts = -F/cinterop/keychain
staticLibraries = libkeychainwrapper.a
libraryPaths = cinterop/keychain" \
>> ".$target_file_dir$target_file_name"