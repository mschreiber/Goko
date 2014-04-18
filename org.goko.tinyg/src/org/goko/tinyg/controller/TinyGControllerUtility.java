/*
 *
 *   Goko
 *   Copyright (C) 2013  PsyKo
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.goko.tinyg.controller;

import org.apache.commons.lang3.StringUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.controller.bean.MachineState;
import org.goko.core.gcode.bean.GCodeCommand;
import org.goko.core.gcode.bean.Tuple6b;
import org.goko.core.log.GkLog;
import org.goko.tinyg.controller.configuration.TinyGConfiguration;
import org.goko.tinyg.controller.configuration.TinyGGroupSettings;
import org.goko.tinyg.controller.configuration.TinyGSetting;
import org.goko.tinyg.json.TinyGJsonUtils;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class TinyGControllerUtility {
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(TinyGControllerUtility.class);

	public TinyGControllerUtility() {

	}

	public final static TinyGConfiguration getConfigurationCopy(final TinyGConfiguration baseCfg) throws GkException{
		TinyGConfiguration copy = new TinyGConfiguration();
		for(TinyGGroupSettings group : baseCfg.getGroups()){
			for(TinyGSetting<?> setting : group.getSettings()){
				copy.setSetting(group.getGroupIdentifier(), setting.getIdentifier(), setting.getValue());
			}
		}
		return baseCfg;
	}

	/**
	 * Handling configuration response from TinyG
	 * @return {@link JsonValue}
	 */
	protected static void handleConfigurationModification(TinyGConfiguration configuration, JsonObject responseEnvelope) throws GkException {
		TinyGJsonUtils.setConfiguration(configuration, responseEnvelope, StringUtils.EMPTY);
		LOG.debug("TinyG handleConfigModification "+responseEnvelope.toString());
	}

	/**
	 * Transform a GCode command to a JsonValue
	 * @param command the command to transform
	 * @return {@link JsonValue}
	 */
	protected static JsonValue toJson(GCodeCommand command){
		JsonObject value = new JsonObject();
		value.add(TinyGJsonUtils.GCODE_COMMAND, StringUtils.lowerCase(command.toString()));
		return value;
	}


	protected static Tuple6b updatePosition(Tuple6b lastKnownPosition, JsonObject statusReport){
		Tuple6b newPosition = new Tuple6b(lastKnownPosition);
		JsonValue newPositionX = statusReport.get(TinyGJsonUtils.STATUS_REPORT_POSITION_X);
		JsonValue newPositionY = statusReport.get(TinyGJsonUtils.STATUS_REPORT_POSITION_Y);
		JsonValue newPositionZ = statusReport.get(TinyGJsonUtils.STATUS_REPORT_POSITION_Z);
		JsonValue newPositionA = statusReport.get(TinyGJsonUtils.STATUS_REPORT_POSITION_A);
		if(newPositionX != null){
			newPosition.setX(newPositionX.asBigDecimal());
		}
		if(newPositionY != null){
			newPosition.setY(newPositionY.asBigDecimal());
		}
		if(newPositionZ != null){
			newPosition.setZ(newPositionZ.asBigDecimal());
		}
		if(newPositionA != null){
			newPosition.setA(newPositionA.asBigDecimal());
		}
		return newPosition;
	}
	/**
	 * Convert Integer value to Machine State
	 * @param stateCode the int value
	 * @return {@link MachineState} object
	 */
	protected static MachineState getState(Integer stateCode){
		switch(stateCode){
		case 0: return MachineState.INITIALIZING;
		case 1: return MachineState.READY;
		case 2: return MachineState.ALARM;
		case 3: return MachineState.PROGRAM_STOP;
		case 4: return MachineState.PROGRAM_END;
		case 5: return MachineState.MOTION_RUNNING;
		case 6: return MachineState.MOTION_HOLDING;
		case 7: return MachineState.PROBE_CYCLE;
		case 8: return MachineState.RUNNING;
		case 9: return MachineState.HOMING;
		default: return MachineState.INITIALIZING;
		}

	}
}