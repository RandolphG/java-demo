FROM artifactory.dev.adskengineer.net/container-hardening/alpine-hardened-min:latest

WORKDIR /tmp
CMD ["/tmp/run_test.sh"]
COPY --chmod=0755 tests/run_test.sh /tmp/
