#!/bin/sh

set -ex
 
cd /usr/local/acmgroups/live/live1-private/import/ip_geography/ || cd /usr/local/acmgroups/test/test1-private/import/ip_geography/

Fetch(){
	local URL="$1"
	local FILE="$2"
	fetch -m -a -o "$FILE" "$URL" ; chmod 666 "$FILE" ; return 0
}

Fetch https://ftp.afrinic.net/pub/stats/afrinic/delegated-afrinic-latest 	delegated-afrinic-latest &
Fetch https://ftp.apnic.net/pub/stats/apnic/delegated-apnic-latest 			delegated-apnic-latest &
Fetch https://ftp.arin.net/pub/stats/arin/delegated-arin-extended-latest 	delegated-arin-latest &
Fetch https://ftp.apnic.net/pub/stats/iana/delegated-iana-latest 			delegated-iana-latest &
Fetch https://ftp.lacnic.net/pub/stats/lacnic/delegated-lacnic-latest 		delegated-lacnic-latest &
Fetch https://ftp.ripe.net/ripe/stats/delegated-ripencc-latest 				delegated-ripencc-latest &

wait

rm delete.when.ready
