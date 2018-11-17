#!/bin/bash

export HEROKU_API_KEY=172f5c46-1a0c-4afa-97b1-e3c775df4f9f
cd index-data; mvn clean heroku:deploy -P stage && cd index-view; mvn clean heroku:deploy -P stage
