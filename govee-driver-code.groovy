/**
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 * History:
 *      09-30-2022  RobF Added debug/trace preferences
 */

import hubitat.helper.ColorUtils

metadata {
    definition (
        name: "Govee LED Strips",
        version: "v1",
        namespace: "robf17",
        author: "Dean Berman",
        description: "Govee LED Strips Integration",
        category: "My Apps",
    ) {

        capability "Switch Level"
        capability "Actuator"
        capability "Color Control"
        capability "ColorMode"
        // capability "Power Meter"
        capability "Switch"
        capability "Refresh"
        capability "Sensor"

        command "setAdjustedColor"
        command "reset"
        command "refresh"
    }

    tiles(scale: 2) {
        multiAttributeTile(name:"switch", type: "lighting", width: 6, height: 4, canChangeIcon: true) {
            tileAttribute ("device.switch", key: "PRIMARY_CONTROL") {
                attributeState "on", label:'${name}', action:"switch.off", icon:"st.lights.philips.hue-single", backgroundColor:"#00A0DC", nextState:"turningOff"
                attributeState "off", label:'${name}', action:"switch.on", icon:"st.lights.philips.hue-single", backgroundColor:"#ffffff", nextState:"turningOn"
                attributeState "turningOn", label:'${name}', action:"switch.off", icon:"st.lights.philips.hue-single", backgroundColor:"#00A0DC", nextState:"turningOff"
                attributeState "turningOff", label:'${name}', action:"switch.on", icon:"st.lights.philips.hue-single", backgroundColor:"#ffffff", nextState:"turningOn"
            }
            tileAttribute ("device.power", key: "SECONDARY_CONTROL") {
                attributeState "power", label:'Power level: ${currentValue}W', icon: "st.Appliances.appliances17"
            }
            tileAttribute ("device.level", key: "SLIDER_CONTROL") {
                attributeState "level", action:"switch level.setLevel"
            }
            tileAttribute ("device.color", key: "COLOR_CONTROL") {
                attributeState "color", action:"setAdjustedColor"
            }
        }

        multiAttributeTile(name:"switchNoPower", type: "lighting", width: 6, height: 4, canChangeIcon: true) {
            tileAttribute ("device.switch", key: "PRIMARY_CONTROL") {
                attributeState "on", label:'${name}', action:"switch.off", icon:"st.lights.philips.hue-single", backgroundColor:"#00A0DC", nextState:"turningOff"
                attributeState "off", label:'${name}', action:"switch.on", icon:"st.lights.philips.hue-single", backgroundColor:"#ffffff", nextState:"turningOn"
                attributeState "turningOn", label:'${name}', action:"switch.off", icon:"st.lights.philips.hue-single", backgroundColor:"#00A0DC", nextState:"turningOff"
                attributeState "turningOff", label:'${name}', action:"switch.on", icon:"st.lights.philips.hue-single", backgroundColor:"#ffffff", nextState:"turningOn"
            }
            tileAttribute ("device.level", key: "SLIDER_CONTROL") {
                attributeState "level", action:"switch level.setLevel"
            }
            tileAttribute ("device.color", key: "COLOR_CONTROL") {
                attributeState "color", action:"setAdjustedColor"
            }
        }

        multiAttributeTile(name:"switchNoSlider", type: "lighting", width: 6, height: 4, canChangeIcon: true) {
            tileAttribute ("device.switch", key: "PRIMARY_CONTROL") {
                attributeState "on", label:'${name}', action:"switch.off", icon:"st.lights.philips.hue-single", backgroundColor:"#00A0DC", nextState:"turningOff"
                attributeState "off", label:'${name}', action:"switch.on", icon:"st.lights.philips.hue-single", backgroundColor:"#ffffff", nextState:"turningOn"
                attributeState "turningOn", label:'${name}', action:"switch.off", icon:"st.lights.philips.hue-single", backgroundColor:"#00A0DC", nextState:"turningOff"
                attributeState "turningOff", label:'${name}', action:"switch.on", icon:"st.lights.philips.hue-single", backgroundColor:"#ffffff", nextState:"turningOn"
            }
            tileAttribute ("device.power", key: "SECONDARY_CONTROL") {
                attributeState "power", label:'The power level is currently: ${currentValue}W', icon: "st.Appliances.appliances17"
            }
            tileAttribute ("device.color", key: "COLOR_CONTROL") {
                attributeState "color", action:"setAdjustedColor"
            }
        }

        multiAttributeTile(name:"switchNoSliderOrColor", type: "lighting", width: 6, height: 4, canChangeIcon: true) {
            tileAttribute ("device.switch", key: "PRIMARY_CONTROL") {
                attributeState "on", label:'${name}', action:"switch.off", icon:"st.lights.philips.hue-single", backgroundColor:"#00A0DC", nextState:"turningOff"
                attributeState "off", label:'${name}', action:"switch.on", icon:"st.lights.philips.hue-single", backgroundColor:"#ffffff", nextState:"turningOn"
                attributeState "turningOn", label:'${name}', action:"switch.off", icon:"st.lights.philips.hue-single", backgroundColor:"#00A0DC", nextState:"turningOff"
                attributeState "turningOff", label:'${name}', action:"switch.on", icon:"st.lights.philips.hue-single", backgroundColor:"#ffffff", nextState:"turningOn"
            }
            tileAttribute ("device.power", key: "SECONDARY_CONTROL") {
                attributeState "power", label:'The light is currently consuming this amount of power: ${currentValue}W', icon: "st.Appliances.appliances17"
            }
        }

        valueTile("color", "device.color", inactiveLabel: false, decoration: "flat", width: 2, height: 2) {
            state "color", label: '${currentValue}'
        }

        standardTile("reset", "device.reset", inactiveLabel: false, decoration: "flat", width: 2, height: 2) {
            state "reset", label:"Reset Color", action:"reset", icon:"st.lights.philips.hue-single", defaultState: true
        }
        standardTile("refresh", "device.switch", inactiveLabel: false, decoration: "flat", width: 2, height: 2) {
            state "refresh", label:"", action:"refresh.refresh", icon:"st.secondary.refresh", defaultState: true
        }

        main(["switch"])
        details(["switch", "switchNoPower", "switchNoSlider", "switchNoSliderOrColor", "color", "refresh", "reset"])
    }
}

preferences {
    section("Govee LED Device") {
        // TODO: put inputs here
        input("apikey", "apikey", title: "Govee API Key", description: "Designated Govee API Key for accessing their system", required: true)
        input("deviceID", "deviceid", title: "Govee KED Device ID", description: "Device ID for the Govee LED Lights", required: true)
        input("modelNum", "modelnum", title: "Govee LED Model", description: "Model number of the Govee LED Lights", required: true)
    }
    input("debug", "bool", title: "Debug",description: "Debug Logging", defaultState: false)
    input("trace", "bool", title: "Trace",description: "Trace Logging", defaultState: false)
}

def logDebug(msg) {
    if (debug) {
        log.debug("${msg}")
    }
}

def logTrace(msg) {
    if (trace) {
        log.trace("${msg}")
    }
}

def refresh() {

    logTrace( "refresh(}..." )

    def ret = callURL('devicestate', '')

    // Handling the power state
    def power = ret.data["properties"][1]["powerState"]
    logDebug( "[refresh(}] - Power=${power}" )
    if(power == 'on') {
        on(false)
    } else {
        off(false)
    }

    // Handling the brightness level
    def brightness = ret.data["properties"][2]["brightness"]
    logDebug( "[refresh()] -- Brightness=${brightness}" )
    setLevel(brightness, false);

    // Handling the color
    List rgb = [ret.data["properties"][3]["color"]["r"], ret.data["properties"][3]["color"]["g"], ret.data["properties"][3]["color"]["b"]]
    logDebug( "[refresh()] --- R=${rgb[0]}, G=${rgb[1]}, B=${rgb[2]}" )
    setColorRGB(rgb, false)

    unschedule()
    // Set it to run once a minute (continuous polling)
    logDebug( "[refresh()] ---- Polling/Refresh every 2 minutes" )
    runIn(120, refresh)
}

// handle commands
def on(callapi=true) {
    logTrace( "on(${callapi})..." )

    if(callapi) {
        logDebug( "[on(}] - Executing callURL for devicecontrol-power = on" )
        callURL('devicecontrol-power', "on")
    }

    logDebug( "[on(}] -- sendEvent to switch = on" )
    sendEvent(name: "switch", value: "on")
}

def off(callapi=true) {
    logTrace( "off(${callapi})..." )

    if(callapi) {
        logDebug( "[off(}] - Executing callURL for devicecontrol-power = off" )
        callURL('devicecontrol-power', "off")
    }

    logDebug( "[off(}] -- sendEvent to switch = off" )
    sendEvent(name: "switch", value: "off")
}
/*
def parse(description) {
    logTrace( "parse(${description})..." )
    
    def results = []
    def map = description
    if (description instanceof String)  {
        log.debug "Hue Bulb stringToMap - ${map}"
        map = stringToMap(description)
    }
    if (map?.name && map?.value) {
        results << createEvent(name: "${map?.name}", value: "${map?.value}")
    }
    results
}
def nextLevel() {
    logTrace( "nextLevel(}..." )
    
    def level = device.latestValue("level") as Integer ?: 0
    if (level <= 100) {
        level = Math.min(25 * (Math.round(level / 25) + 1), 100) as Integer
    }
    else {
        level = 25
    }
    setLevel(level, true)
}
*/
def setSaturation(percent) {
    logTrace( "setSaturation(${percent})..." )

    log.info "[setSaturation()] - Govee API does not support SATURATION at this time, so this value is being set within Hubitat"

    //logDebug( "[setSaturation(}] - saturation = ${percent)" )
    sendEvent(name: "saturation", value: percent)
}

def setHue(percent) {
    logTrace( "setHue(${percent})..." )

    log.info "[setHue()] - Govee API does not support HUE at this time, so this value is being set within Hubitat"

    //logDebug( "[setHue(}] - HUE = ${percent)" )
    sendEvent(name: "hue", value: percent)
}

def setLevel(percent, callapi=true) {
    logTrace( "setLevel(${percent}, ${callapi})..." )

    logDebug( "[setLevel()] - sendEvent level = ${percent}" )
    sendEvent(name: "level", value: percent)
    def power = Math.round(percent / 1.175) * 0.1

    if(callapi) {
        logDebug( "[setLevel()] -- Executing callURL for devicecontrol-brightness = ${percent}" )
        callURL('devicecontrol-brightness', percent)
    }
}
def setColorRGB(rgb, callapi=true) {
    logTrace( "setColorRGB(${rgb}, ${callapi})..." )

    // Handling the color
    logDebug( "[setColorRGB()] - R=${rgb[0]}, G=${rgb[1]}, B=${rgb[2]}" )
    //def hex = colorUtil.rgbToHex(rgb[0], rgb[1], rgb[2])
    def hex = String.format("#%02X%02X%02X", rgb[0], rgb[1], rgb[2])
    logDebug( "[setColorRGB()] -- HEX=${hex}" )
    sendEvent(name: "color", value: hex)

    if(callapi) {
        logDebug( "[setColorRGB()] --- Executing callURL for devicecontrol-rgb = ${rgb}" )
        callURL('devicecontrol-rgb', rgb)
    }
}
def setColor(Map value) {
    logTrace( "setColor(${value})..." )

    // Handling the color
    logDebug( "[setColor()] - H=${value.hue}, S=${value.saturation}, L=${value.level}" )

    if(value.level == null) {
        value.level = device.currentValue("level")
        logDebug( "[setColor(}] -- RESET HSL - H=${value.hue}, S=${value.saturation}, L=${value.level}" )
    }

    def rgb = hsl2rgb(value.hue, value.saturation, value.level)

    // Handling the color
    logDebug( "[setColor()] -- R=${rgb[0]}, G=${rgb[1]}, B=${rgb[2]}" )
    def hex = String.format("#%02X%02X%02X", rgb[0], rgb[1], rgb[2])

    logDebug( "[setColor()] --- HEX=${hex}" )
    sendEvent(name: "color", value: hex)

    logDebug( "[setColor()] ---- Executing callURL for devicecontrol-rgb = ${rgb}" )
    callURL('devicecontrol-rgb', rgb)
}

def hsl2rgb(H, S, L) {
    logTrace( "hsl2rgb(${H}, ${S}, ${L})..." )

    logDebug( "[hsl2rgb()] - Getting RGB..." )
    List rgb = ColorUtils.hsvToRGB([H, S, L])
    logDebug( "[hsl2rgb()] -- R=${rgb[0]}, G=${rgb[1]}, B=${rgb[2]}..." )

    return rgb
}
def hue2rgb(P, Q, T) {
    logTrace( "hue2rgb(${P}, ${Q}, ${T})..." )

    logDebug( "[hue2rgb(}] - Calculating values..." )

    if(T < 0) T += 1;
    if(T > 1) T -= 1;
    if(T < 1/6) return P + (Q - P) * 6 * T;
    if(T < 1/2) return Q;
    if(T < 2/3) return P + (Q - P) * (2/3 - T) * 6;
    return P;
}
def reset() {
    logTrace( "reset()..." )

    def ret = callURL('devicestate', '')

    // Handling the power state
    logDebug( "[reset()] - Power=on" )
    on(true)

    // Handling the brightness level
    logDebug( "[refresh()] -- Brightness=100" )
    setLevel(100, true);

    // Handling the color
    List rgb = [255, 255, 255]
    logDebug( "[refresh()] --- R=${rgb[0]}, G=${rgb[1]}, B=${rgb[2]}" )
    setColorRGB(rgb, true)
}

def setAdjustedColor(value) {
    logTrace( "setAdjustedColor(${value})..." )

    log.error "[setSaturation()] - Sorry but the Govee API does not support SATURATION at this time, so this is being skipped"

/*
    if (value) {
        log.trace "setAdjustedColor: ${value}"
        def adjusted = value + [:]
        adjusted.hue = adjustOutgoingHue(value.hue)
        // Needed because color picker always sends 100
        adjusted.level = null
        setColor(adjusted)
    }
*/
}

def installed() {
    logTrace( "installed(}..." )

    refresh()
}

def updated() {
    logTrace( "updated(}..." )

    refresh()

    // unschedule()
    // runEvery1Minutes(refresh)
    // runIn(2, refresh)
}

def poll() {
    logTrace( "poll(}..." )

    refresh()
}

def adjustOutgoingHue(percent) {
    logTrace( "adjustOutgoingHue(${percent})..." )

    log.error "[adjustOutgoingHue()] - Sorry but the Govee API does not support HUE at this time, so this is being skipped"

    /*
        def adjusted = percent
        if (percent > 31) {
            if (percent < 63.0) {
                adjusted = percent + (7 * (percent -30 ) / 32)
            }
            else if (percent < 73.0) {
                adjusted = 69 + (5 * (percent - 62) / 10)
            }
            else {
                adjusted = percent + (2 * (100 - percent) / 28)
            }
        }
        log.info "percent: $percent, adjusted: $adjusted"
        adjusted
    */
}

// TODO: implement event handlers
def callURL(apiAction, details) {
    logTrace( "callURL(${apiAction}, ${details})..." )

    logDebug( "[callURL()] - APIKEY=${settings.apikey}, ID=${settings.deviceID}, MODEL=${settings.modelNum}" )

    def method
    def params
    if(apiAction == 'devices') {
        method = 'GET'
        params = [
            uri   : "https://developer-api.govee.com",
            path  : '/v1/devices',
            headers: ["Govee-API-Key": settings.apikey, "Content-Type": "application/json"],
        ]
    } else if(apiAction == 'devicestate') {
        method = 'GET'
        params = [
            uri   : "https://developer-api.govee.com",
            path  : '/v1/devices/state',
            headers: ["Govee-API-Key": settings.apikey, "Content-Type": "application/json"],
            query: [device: settings.deviceID, model: settings.modelNum],
        ]
    } else if(apiAction == 'devicecontrol-power') {
        method = 'PUT'
        params = [
            uri   : "https://developer-api.govee.com",
            path  : '/v1/devices/control',
            headers: ["Govee-API-Key": settings.apikey, "Content-Type": "application/json"],
            contentType: "application/json",
            body: [device: settings.deviceID, model: settings.modelNum, cmd: ["name": "turn", "value": details]],
        ]
    } else if(apiAction == 'devicecontrol-brightness') {
        method = 'PUT'
        params = [
            uri   : "https://developer-api.govee.com",
            path  : '/v1/devices/control',
            headers: ["Govee-API-Key": settings.apikey, "Content-Type": "application/json"],
            contentType: "application/json",
            body: [device: settings.deviceID, model: settings.modelNum, cmd: ["name": "brightness", "value": details]],
        ]
    } else if(apiAction == 'devicecontrol-rgb') {
        method = 'PUT'
        params = [
            uri   : "https://developer-api.govee.com",
            path  : '/v1/devices/control',
            headers: ["Govee-API-Key": settings.apikey, "Content-Type": "application/json"],
            contentType: "application/json",
            body: [device: settings.deviceID, model: settings.modelNum, cmd: ["name": "color", "value": ["r": details[0], "g": details[1], "b": details[2]]]],
        ]
    }

    if (debug) {
        /*
            log.debug params
            log.debug "APIACTION=${apiAction}"
            log.debug "METHOD=${method}"
            log.debug "URI=${params.uri}${params.path}"
            log.debug "HEADERS=${params.headers}"
            log.debug "QUERY=${params.query}"
            log.debug "BODY=${params.body}"
        */
    }
    try {
       if(method == 'GET') {
            httpGet(params) { resp ->
                //log.debug "RESP="
                //log.debug "HEADERS="+resp.headers
                //log.debug "DATA="+resp.data

                logDebug( "[callURL(}] -- response.data="+resp.data )

                return resp.data
            }
        } else if(method == 'PUT') {
            httpPutJson(params) { resp ->
                //log debug "RESP="
                //log.debug "HEADERS="+resp.headers
                //log.debug "DATA="+resp.data

                logDebug( "[callURL(}] -- response.data="+resp.data )

                return resp.data
            }
        }
    } 
    catch (groovyx.net.http.HttpResponseException e) {
        log.error "callURL() >>>>>>>>>>>>>>>> ERROR >>>>>>>>>>>>>>>>"
        log.error "Error: e.statusCode ${e.statusCode}"
        log.error "${e}"
        log.error "callURL() <<<<<<<<<<<<<<<< ERROR <<<<<<<<<<<<<<<<"

        return false
    }
}
