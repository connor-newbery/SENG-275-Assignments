#!/bin/bash
 

fail_check[0]=$(./tester4.py --start=2021/2/14 --end=2021/2/14 --file=one.ics | diff test01.txt -)
fail_check[1]=$(./tester4.py --start=2021/4/18 --end=2021/4/21 --file=two.ics | diff test02.txt -)
fail_check[2]=$(./tester4.py --start=2021/2/1 --end=2021/3/1 --file=many.ics | diff test03.txt -)
fail_check[3]=$(./tester4.py --start=2021/4/22 --end=2021/4/23 --file=two.ics | diff test04.txt -)
fail_check[4]=$(./tester4.py --start=2021/2/1 --end=2021/2/1 --file=diana-devops.ics | diff test05.txt -)
fail_check[5]=$(./tester4.py --start=2021/2/2 --end=2021/2/2 --file=diana-devops.ics | diff test06.txt -)
fail_check[6]=$(./tester4.py --start=2021/2/1 --end=2021/2/8 --file=diana-devops.ics | diff test07.txt -)
fail_check[7]=$(./tester4.py --start=2021/2/8 --end=2021/2/15 --file=diana-devops.ics | diff test08.txt -)
fail_check[8]=$(./tester4.py --start=2021/2/1 --end=2021/3/1 --file=diana-devops.ics | diff test09.txt -)
fail_check[9]=$(./tester4.py --start=2021/1/1 --end=2021/4/30 --file=diana-devops.ics | diff test10.txt -)
fail_check[10]=$(./tester4.py --start=2021/5/1 --end=2021/6/1 --file=mlb.ics | diff test11.txt -)
fail_check[11]=$(./tester4.py --start=2021/5/1 --end=2021/8/1 --file=mlb.ics | diff test12.txt -)
fail_check[12]=$(./tester4.py --start=2020/12/1 --end=2021/4/30 --file=mlb.ics | diff test13.txt -)
 
n=1
for i in "${fail_check[@]}"
do
    echo ====================
    if ["$i" == ""]
    then
        echo Test "$n" passed
    else
        echo Test "$n" failed
    fi
    echo ==================== 
    echo
 
    let "n++"
done