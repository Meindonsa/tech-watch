compile:
	mvn clean install -DskipTests -T1C

run:
	mvn spring-boot:run

_push_github:
	git push github master

_push_bitbucket:
	git push bitbucket master

push_all: _push_bitbucket _push_github