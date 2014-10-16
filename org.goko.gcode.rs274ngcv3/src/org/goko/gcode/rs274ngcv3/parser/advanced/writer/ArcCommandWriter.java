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
package org.goko.gcode.rs274ngcv3.parser.advanced.writer;

import org.apache.commons.lang3.StringUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.Tuple6b;
import org.goko.core.gcode.bean.commands.ArcMotionCommand;

public class ArcCommandWriter extends AbstractMotionCommandWriter<ArcMotionCommand>{

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.parser.advanced.writer.AbstractRS274CommandWriter#write(org.goko.core.gcode.bean.GCodeCommand)
	 */
	@Override
	public String write(ArcMotionCommand command) throws GkException {
		String str = super.write(StringUtils.EMPTY, command);
		str = writeOffsets(str, command);
		return str;
	}

	/**
	 * Writes I,J and K offsets
	 * @param str the base string
	 * @param command the command
	 */
	private String writeOffsets(String str, ArcMotionCommand command) {
		Tuple6b offsets = command.getAbsoluteCenterCoordinate().subtract(command.getAbsoluteStartCoordinate());
		if(offsets.getX() != null){
			str += " I"+offsets.getX();
		}
		if(offsets.getY() != null){
			str += " J"+offsets.getY();
		}
		if(offsets.getZ() != null){
			str += " K"+offsets.getZ();
		}
		return str;
	}

}
