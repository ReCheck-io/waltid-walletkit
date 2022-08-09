#!/bin/bash
./gradlew build install


cp -b ./service-matrix.properties ./build/install/waltid-walletkit/bin/
cp -b ./signatory.conf ./build/install/waltid-walletkit/bin/



