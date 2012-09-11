#!/bin/bash
FAILED=0
cd ./SoomlaAndroidStore
ant clean
ant debug
if [ "$?" = 1 ]; then
    echo "SoomlaAndroidStore build failed!"
    FAILED=1
fi
cd ..


# cd ./SoomlaAndroidStoreTest
# ant clean
# ant test
# if [ "$?" = 1 ]; then
	# echo "SoomlaAndroidStoreTest build failed!"
    # FAILED=1
# fi
# cd ..
exit $FAILED
