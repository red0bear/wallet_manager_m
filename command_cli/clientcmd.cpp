#include <cstdio>
#include <iostream>
#include <memory>
#include <stdexcept>
#include <string>
#include <cstring>
#include <array>

using namespace std;

string result;

extern "C"
{
	char* cmdtst(const char* cmd);
}

char* cmdtst(const char* cmd) 
{
    char * cstr = NULL;
    result.clear();

    std::array<char, 128> buffer;
    std::unique_ptr<FILE, decltype(&pclose)> pipe(popen(cmd, "r"), pclose);
    if (!pipe) {
        throw std::runtime_error("popen() failed!");
    }
    while (fgets(buffer.data(), buffer.size(), pipe.get()) != nullptr) {
        result += buffer.data();
    }

   cstr = new char [result.length()+1];
   std::strcpy (cstr, result.c_str());
 
    return &cstr[0];
}


