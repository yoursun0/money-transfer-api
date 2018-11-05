#!/bin/bash

dbName="MoneyTransfer"
dbUser=""
dbPassword=""

echo "clean all existing data in MoneyTransfer database..."
mongo $dbName -u $dbUser -p $dbPassword --authenticationDatabase $dbName --quiet --eval "db.Account.remove({})"

echo "loading initial account data..."

createTs=`echo $(date --iso-8601=seconds)`

for acc in `ls ./mongo/init/accounts.json`
do
	mongo $dbName -u $dbUser -p $dbPassword --authenticationDatabase $dbName --quiet --eval "db.Account.insert(`cat $acc`)"
done
mongo $dbName -u $dbUser -p $dbPassword --authenticationDatabase $dbName --quiet --eval "db.Account.update({},{\$set : {'createTs':ISODate('`$createTs`')}},false,true)"
mongo $dbName -u $dbUser -p $dbPassword --authenticationDatabase $dbName --quiet --eval "db.Account.update({},{\$set : {'updateTs':ISODate('`$createTs`')}},false,true)"

echo "load initial data completed"
