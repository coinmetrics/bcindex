Index data is responsible for
 - grabbing data from public data sources (ie. coincap)
 - pulling the rebalance files from local disk
 - using the two above data sources to calculate the indexes
 - persisting the index data to a relational db

Other secondary responsiblities include 
 - update the index-view cache via a rest call (price/weights) 
 - update the index-api index values with a rest call (weights)


Dependencies:
    to get a full list run $ mvn dependency:tree
    Within the project index-data uses core-db
    to hold most of its database logic. 
    
    Index-data also depends on core for some
    shared time and index objects