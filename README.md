# OSPicture [![Build Status](https://travis-ci.org/MrChebik/OSPicture.svg?branch=master)](https://travis-ci.org/MrChebik/OSPicture)
> Hosting the [images](http://ospicture.xyz/image/31b548211f) with [multiply uploads](http://ospicture.xyz/folder/08lahwa3cv), lossless optimization and less instances.

## Getting Started
1. Setting _utf8_ encoding in _MySQL_:
    1. Go to _/etc/my.cnf_
    2. Remove ___;___ before _collation_server_ and _character_set_server_
2. Install image optimizations libraries and ImageMagick:
    * My tips for ___RPM___ (_CentOS6_ and _Mageia_): [mozjpeg](https://gist.github.com/MrChebik/d5cd2920d49415122376ef2f600907ce) and [optipng](https://gist.github.com/MrChebik/8c3594a521898b889d8acf4f419cbcbc)
    * Also you can find manuals on their pages:
        [ImageMagick](https://github.com/ImageMagick/ImageMagick)
        [mozjpeg](https://github.com/mozilla/mozjpeg)
        [optipng](http://optipng.sourceforge.net)

## Run
1. Execute in folder project:
```
mvn spring-boot:run \
--Dspring.datasource.username=USERNAME_DB \
--Dspring.datasource.password=PASSWORD_DB \
--Dpath.pictures=FOLDER_TO_STORE_IMAGES
```
* You can change properties in [__application.properties__](https://github.com/MrChebik/OSPicture/blob/master/src/main/resources/application.properties) and run without this parameters.
2. Follow to the link: [_http://localhost:8446/_](http://localhost:8446/)

## License
This project is licensed under the GPL 3.0 License - see the [LICENSE](https://github.com/MrChebik/OSPicture/blob/master/LICENSE) file for details.