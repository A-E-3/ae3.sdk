#!/bin/sh

cd /usr/local/acmgroups/live/live1-private/import/ip_geography/ || cd /usr/local/acmgroups/test/test1-private/import/ip_geography/

fetch ftp://ftp.afrinic.net/pub/stats/afrinic/delegated-afrinic-latest
fetch ftp://ftp.apnic.net/pub/stats/apnic/delegated-apnic-latest
fetch -o delegated-arin-latest http://ftp.arin.net/pub/stats/arin/delegated-arin-extended-latest
fetch ftp://ftp.apnic.net/pub/stats/iana/delegated-iana-latest
fetch ftp://ftp.lacnic.net/pub/stats/lacnic/delegated-lacnic-latest
fetch ftp://ftp.ripe.net/ripe/stats/delegated-ripencc-latest

rm delete.when.ready
