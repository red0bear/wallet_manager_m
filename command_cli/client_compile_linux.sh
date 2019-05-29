g++ -c -Wall -fpic clientcmd.cpp

g++ -v -fPIC  -dynamiclib -shared -o clientcmd.so clientcmd.o
