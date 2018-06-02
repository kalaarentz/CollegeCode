


CPPFLAGS=/I Pkgs\OpenVR\include /I Pkgs\SDL2-2.0.5\include /I Pkgs\glew-2.1.0\include /D Windows /EHsc /ZI /nologo /W3 /WX- /diagnostics:classic /Od /Oy- /D WIN32 /D _CRT_SECURE_NO_WARNINGS /D _CRT_NONSTDC_NO_DEPRECATE /D _DEBUG /D _WINDOWS /D _UNICODE /D UNICODE /Gm /RTC1 /MTd /GS /fp:precise /Zc:wchar_t /Zc:forScope /Zc:inline /Gd /TP /analyze-

hw-6.exe: main.obj Draw.obj shaderUtil.obj model.obj OBJModel.obj stdModels.obj matrixUtil.obj modelVAO.obj lodepng.obj Matrices.obj pathtools.obj
		link.exe /SUBSYSTEM:CONSOLE /OUT:$@ /LIBPATH:Pkgs\OpenVR\lib\ /LIBPATH:Pkgs\SDL2-2.0.5\lib\x86 $** SDL2.lib /LIBPATH:Pkgs\glew-2.1.0\lib/Release/Win32 glew32.lib opengl32.lib openvr_api.lib SDL2main.lib

all:  hw-6.exe

clean:
    del *.obj *.exe *.idb *.pdb
