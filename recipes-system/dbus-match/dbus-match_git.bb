DESCRIPTION = "Tool for catching dbus-signals with a timeout. \
				If you don't need the timeout, prefer dbus-monitor"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRCREV ?= "fd94de1e149ddda488ed936bbb517e82c14bfab8"
SRC_URI = "git://git@bitbucket.datarespons.com:7999/ms/dbus-match.git;protocol=ssh;branch=${BRANCH}"
BRANCH ?= "master"

PV = "git${SRCPV}"
S = "${WORKDIR}/git"
RDEPENDS_${PN} = "systemd"
DEPENDS = "systemd"

do_install () {
	install -d ${D}${prefix}/bin
	oe_runmake PREFIX=${prefix} DESTDIR=${D} install
}
