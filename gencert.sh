#!/bin/bash

rm -rf cert
mkdir -p cert

openssl genrsa -out ./cert/access.priv 4096
openssl rsa -in ./cert/access.priv -pubout -out ./cert/access.pub

openssl genrsa -out ./cert/refresh.priv 4096
openssl rsa -in ./cert/refresh.priv -pubout -out ./cert/refresh.pub
