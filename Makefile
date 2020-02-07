.PHONY: master develop pir

master:
	git submodule update --init
	cd spatial && git checkout master -- && make 

develop:
	git submodule update --init
	cd spatial && git checkout develop -- && make 

pir:
	git submodule update --init
	cd spatial && git checkout pir -- && make pir
