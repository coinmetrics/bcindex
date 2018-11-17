#!/bin/bash

cd index-data; mvn clean heroku:deploy -P stage
cd index-view; mvn clean heroku:deploy -P stage
