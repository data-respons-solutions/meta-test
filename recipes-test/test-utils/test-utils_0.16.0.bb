DESCRIPTION = "Data Respons test utilities"
LICENSE = "CLOSED"

SRCREV ?= "8c3052e101980577eced7339fcda58764ee6a44f"
SRC_URI = "gitsm://git@github.com/data-respons-solutions/test-utils.git;protocol=ssh;branch=${BRANCH}"
BRANCH ?= "main"

inherit systemd

PACKAGES += "${PN}-bluetooth ${PN}-audio"

DEPENDS = "libiio systemd libinput sdbus-c++ sdbus-c++-tools-native"
RDEPENDS:${PN}:append = " bash bc python3-core"
# coreutils for "date" and "timeout"
RDEPENDS:${PN}:append = " coreutils"
# python3 for dir-checksum
RDEPENDS:${PN}:append = " python3-crypt"
# test-gpio for libgpiod-tools
RDEPENDS:${PN}:append = " libgpiod-tools"
FILES:${PN} = "${bindir}/memsize ${bindir}/memalloc ${bindir}/iio-read ${bindir}/validate-nvram \
			   ${bindir}/retry-until ${bindir}/verify-pattern ${bindir}/dir-checksum \
			   ${bindir}/test-gpio ${bindir}/serial-echo ${bindir}/pwm-beeper ${bindir}/ip-echo \
			   ${sysconfdir}/test-utils/ ${bindir}/gpsd-fix ${bindir}/backlight-increment \
			   ${bindir}/input-detect"

RDEPENDS:${PN}-bluetooth = "python3 python3-pygobject python3-dbus bluez5"
FILES:${PN}-bluetooth = "${bindir}/bt-agent ${bindir}/bt-spp-echo"

RDEPENDS:${PN}-audio = "gstreamer1.0 pulseaudio-server gstreamer1.0-plugins-base-audiotestsrc gstreamer1.0-plugins-good-pulseaudio gstreamer1.0-plugins-good-wavparse alsa-utils-speakertest"
FILES:${PN}-audio = "${bindir}/speaker-output"

S = "${WORKDIR}/git"

EXTRA_OEMAKE = "BUILD=${WORKDIR}/build"

do_install () {
	install -d ${D}${bindir}
	install -m 0755 ${WORKDIR}/build/memsize ${D}${bindir}/
	install -m 0755 ${WORKDIR}/build/memalloc ${D}${bindir}/
	install -m 0755 ${WORKDIR}/build/iio-read ${D}${bindir}/
	install -m 0755 ${S}/validate-nvram.sh ${D}${bindir}/validate-nvram
	install -m 0755 ${S}/retry-until.sh ${D}${bindir}/retry-until
	install -m 0755 ${WORKDIR}/build/verify-pattern ${D}${bindir}/
	install -m 0755 ${S}/dir-checksum.py ${D}${bindir}/dir-checksum
	install -m 0755 ${S}/test-gpio.sh ${D}${bindir}/test-gpio
	install -m 0755 ${WORKDIR}/build/serial-echo ${D}${bindir}/
	install -m 0755 ${WORKDIR}/build/bt-spp-echo ${D}${bindir}/
	install -m 0755 ${WORKDIR}/build/bt-agent ${D}${bindir}
	install -m 0755 ${WORKDIR}/build/pwm-beeper ${D}${bindir}/
	sed 's:@BINDIR@:${bindir}:g' ${S}/bt-agent.service.in > ${WORKDIR}/build/bt-agent.service
	sed 's:@BINDIR@:${bindir}:g' ${S}/bt-spp-echo.service.in > ${WORKDIR}/build/bt-spp-echo.service
	install -d ${D}${systemd_system_unitdir}
	install -m 0644 ${WORKDIR}/build/bt-agent.service ${D}${systemd_system_unitdir}/
	install -m 0644 ${WORKDIR}/build/bt-spp-echo.service ${D}${systemd_system_unitdir}/
	sed -e 's:@BINDIR@:${bindir}:g' \
		-e 's:@SYSCONFDIR@:${sysconfdir}:g' \
		 ${S}/serial-echo@.service.in > ${WORKDIR}/build/serial-echo@.service
	install -m 0644 ${WORKDIR}/build/serial-echo@.service ${D}${systemd_system_unitdir}/
	install -d ${D}${sysconfdir}/test-utils/serial-echo
	install -m 0755 ${WORKDIR}/build/ip-echo ${D}${bindir}/
	sed -e 's:@BINDIR@:${bindir}:g' \
		-e 's:@SYSCONFDIR@:${sysconfdir}:g' \
		 ${S}/ip-echo@.service.in > ${WORKDIR}/build/ip-echo@.service
	install -m 0644 ${WORKDIR}/build/ip-echo@.service ${D}${systemd_system_unitdir}/
	install -d ${D}${sysconfdir}/test-utils/ip-echo
	install -m 0755 ${S}/gpsd-fix.py ${D}${bindir}/gpsd-fix
	install -m 0755 ${S}/backlight-increment.sh ${D}${bindir}/backlight-increment
	install -m 0755 ${WORKDIR}/build/input-detect ${D}${bindir}/
	install -m 0755 ${S}/speaker-output.sh ${D}${bindir}/speaker-output
}

# Generated sdbus-c++ headers are placed in build-dir and the header path is included in CXXFLAGS.
# Disable buildpaths warning.
WARN_QA:remove = "buildpaths"


SYSTEMD_PACKAGES = "${PN} ${PN}-bluetooth"
SYSTEMD_SERVICE:${PN} = "serial-echo@.service ip-echo@.service"
SYSTEMD_SERVICE:${PN}-bluetooth = "bt-agent.service bt-spp-echo.service"
SYSTEMD_AUTO_ENABLE = "disable"
