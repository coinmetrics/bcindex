#!/bin/bash

cd index-data; mvn clean heroku:deploy -P prod && cd ../index-view; mvn clean heroku:deploy -P prod
