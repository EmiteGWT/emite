/*
 *
 * suco: Mini IoC framework a-la-guice style for GWT
 *
 * (c) 2009 The suco development team (see CREDITS for details)
 * This file is part of emite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.calclab.suco.client.ioc.module;

import com.calclab.suco.client.ioc.Container;

/**
 * A module is a simple object that installs components inside a container.
 * Normally, you extends AbstractModule
 * 
 * @see AbstractModule
 */
public interface SucoModule {

    /**
     * Install the components into the given container
     * 
     * @param the
     *            container
     */
    public void onInstall(Container container);
}
