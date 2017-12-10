#!/bin/bash

set -ex

./gradlew clean build signArchives upload
