# wallet_manager_m
java front end to handle telegram and wallets deamons


# purpose

Simple portfolio manager for cryptocurrencies. Instead of using several wallets with different frontends, concentrate on a single screen.

# java run
To run this app you need https://www.oracle.com/technetwork/java/javase/downloads/index.html?ssSourceSiteId=otnpt

```
java -version

openjdk version "11.0.2" 2018-10-16
OpenJDK Runtime Environment (build 11.0.2+7-suse-lp150.2.12.1-x8664)
OpenJDK 64-Bit Server VM (build 11.0.2+7.2.12.1-x8664, mixed mode)

java -jar wallet_manager_m-1.0.jar

```

# native client access via jna

For using client access is necessary compile terminal C++ access to execute command to return a string json with info included.Files are in :

https://github.com/red0bear/wallet_manager_m/tree/master/command_cli

Must be compiled in shared object where java can access it via Java Native Access. In java you need to point the correct place      like in /client where is necessary wallet-cli.     

```
Native.load((Platform.isLinux()? "/command_cli/clientcmd.so" : "c"),

```

# wallet deamon

Wallet deamon command using old rpc standart

```
  your_walletd -listen=0 -server=1 -deamon=1 -rpcallowip=192.168.xx.x -rpcuser=testuser -rpcpassword=testpass -rpcport=8580
 
```

Wallet deamon command using last rpc standart

```
  your_walletd -listen=0 -server=1 -depreceatedrpc=accounts -rpcallowip=192.168.xx.x -rpcuser=testuser -rpcpassword=testpass -rpcport=8580
 
```

# create a telegram 

This explain how to create bots and handle them --> https://core.telegram.org/bots#6-botfather

 
