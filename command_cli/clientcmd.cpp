#include <cstdio>
#include <iostream>
#include <memory>
#include <stdexcept>
#include <string>
#include <cstring>
#include <array>

using namespace std;

string result;
string result_s;

extern "C"
{
	char* cmd_cli_exec(const char* cmd);
	char* cmd_cli_exec_s(const char* cmd);
}


char* cmd_cli_exec(const char* cmd) 
{

   // std::cout << &cmd[0] <<endl;

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


char* cmd_cli_exec_s(const char* cmd) 
{

   // std::cout << &cmd[0] <<endl;

    char * cstr = NULL;
    result_s.clear();

    std::array<char, 128> buffer;
    std::unique_ptr<FILE, decltype(&pclose)> pipe(popen(cmd, "r"), pclose);
    if (!pipe) {
        throw std::runtime_error("popen() failed!");
    }
    while (fgets(buffer.data(), buffer.size(), pipe.get()) != nullptr) {
        result_s += buffer.data();
    }

   cstr = new char [result_s.length()+1];
   std::strcpy (cstr, result_s.c_str());
 
    return &cstr[0];
}



