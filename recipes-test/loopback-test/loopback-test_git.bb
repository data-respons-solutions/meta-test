DESCRIPTION = "Data Respons loopback interface tests"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRCREV ?= "9687ef7098111b6b5814d20777852b1834403c09"
SRC_URI = "git://git@bitbucket.datarespons.com:7999/ms/loopback-test.git;protocol=ssh;branch=${BRANCH}"
BRANCH ?= "master"

PV = "git${SRCPV}"
S = "${WORKDIR}/git"
RDEPENDS_${PN} += "libusb1"
DEPENDS = "libbyte libusb1"

CXXFLAGS += "-Wno-stringop-truncation"

do_install () {
	install -d ${D}${prefix}/bin
	oe_runmake PREFIX=${prefix} DESTDIR=${D} install
}
