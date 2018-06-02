cmake_minimum_required(VERSION 3.8)
project(Project)

set(CMAKE_CXX_STANDARD 11)

set(SOURCE_FILES
        IOMngr.c
        IOMngr.h
        IOMngrDriver.c
        )

add_executable(Project ${SOURCE_FILES})

#SymTab.c
#SymTab.h
#SymTabDriver.c