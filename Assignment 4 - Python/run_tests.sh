#!/bin/bash

echo -n -n Test1:
if ./tester4.py --start=2021/2/14 --end=2021/2/14 --file=one.ics | diff test01.txt -
then
    echo " pass"
else
    echo " fail"
fi
./tester4.py --start=2021/2/14 --end=2021/2/14 --file=one.ics > out1.txt

echo -n Test2:
if ./tester4.py --start=2021/4/18 --end=2021/4/21 --file=two.ics | diff test02.txt -
then
    echo " pass"
else
    echo "fail"
fi
./tester4.py --start=2021/4/18 --end=2021/4/21 --file=two.ics > out2.txt

echo -n Test3:
if ./tester4.py --start=2021/2/1 --end=2021/3/1 --file=many.ics | diff test03.txt -
then
    echo " pass"
else
    echo "fail"
fi
./tester4.py --start=2021/2/1 --end=2021/3/1 --file=many.ics > out3.txt

echo -n Test4:
if ./tester4.py --start=2021/4/22 --end=2021/4/23 --file=two.ics | diff test04.txt -
then
    echo " pass"
else
    echo "fail"
fi
./tester4.py --start=2021/4/22 --end=2021/4/23 --file=two.ics > out4.txt

echo -n Test5:
if ./tester4.py --start=2021/2/1 --end=2021/2/1 --file=diana-devops.ics | diff test05.txt -
then
    echo " pass"
else
    echo "fail"
fi
./tester4.py --start=2021/2/1 --end=2021/2/1 --file=diana-devops.ics > out5.txt

echo -n Test6:
if ./tester4.py --start=2021/2/2 --end=2021/2/2 --file=diana-devops.ics | diff test06.txt -
then
    echo " pass"
else
    echo "fail"
fi
./tester4.py --start=2021/2/2 --end=2021/2/2 --file=diana-devops.ics > out6.txt

echo -n Test7:
if ./tester4.py --start=2021/2/1 --end=2021/2/8 --file=diana-devops.ics | diff test07.txt -
then
    echo " pass"
else
    echo "fail"
fi
./tester4.py --start=2021/2/1 --end=2021/2/8 --file=diana-devops.ics > out7.txt

echo -n Test8:
if ./tester4.py --start=2021/2/8 --end=2021/2/15 --file=diana-devops.ics | diff test08.txt -
then
    echo " pass"
else
    echo "fail"
fi
./tester4.py --start=2021/2/8 --end=2021/2/15 --file=diana-devops.ics > out8.txt

echo -n Test9:
if ./tester4.py --start=2021/2/1 --end=2021/3/1 --file=diana-devops.ics | diff test09.txt -
then
    echo " pass"
else
    echo "fail"
fi
./tester4.py --start=2021/2/1 --end=2021/3/1 --file=diana-devops.ics > out9.txt

echo -n Test10:
if ./tester4.py --start=2021/1/1 --end=2021/4/30 --file=diana-devops.ics | diff test10.txt -
then
    echo " pass"
else
    echo "fail"
fi
./tester4.py --start=2021/1/1 --end=2021/4/30 --file=diana-devops.ics > out10.txt

echo -n test11:
if ./tester4.py --start=2021/5/1 --end=2021/6/1 --file=mlb.ics | diff test11.txt -
then
    echo " pass"
else
    echo "fail"
fi
./tester4.py --start=2021/5/1 --end=2021/6/1 --file=mlb.ics > out11.txt

echo -n test12:
if ./tester4.py --start=2021/5/1 --end=2021/8/1 --file=mlb.ics | diff test12.txt -
then
    echo " pass"
else
    echo "fail"
fi
./tester4.py --start=2021/5/1 --end=2021/8/1 --file=mlb.ics > out12.txt

echo -n test13:
if ./tester4.py --start=2020/12/1 --end=2021/4/30 --file=mlb.ics | diff test13.txt -
then
    echo " pass"
else
    echo "fail"
fi
./tester4.py --start=2020/12/1 --end=2021/4/30 --file=mlb.ics > out13.txt

