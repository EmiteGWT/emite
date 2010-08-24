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
package com.calclab.suco.testing.events;

/**
 * A listener test helper object. Every listener that implements this interface
 * is susceptible to be queried about itself state
 */
public interface VerificableListener {

    /**
     * Clear the listener state (as is were just created). After this call
     * getCalledTimes retunrs 0.
     */
    public void clear();

    /**
     * @return the number of times this listener is called
     */
    public int getCalledTimes();

    /**
     * @return true if this listener is called one or more times
     */
    public boolean isCalled();

    /**
     * @param timesCalled
     * @return true if this listener is called exactly timesCalled times
     */
    public boolean isCalled(final int timesCalled);

    /**
     * @return true if this listener is called one and only one time
     */
    public boolean isCalledOnce();

    /**
     * Checks if the parameters given are equals to the parameters received in
     * each listener call. See different implementation javadoc
     * 
     * @param expectedOrderedValues
     * @return true if the values are equals
     */
    public boolean isCalledWithEquals(Object... expectedOrderedValues);

    /**
     * Checks if the parameters given are same to the parameters received in
     * each listener calls. See different implementation javadoc
     * 
     * @param expectedOrderedValues
     * @return
     */
    public boolean isCalledWithSame(Object... expectedOrderedValues);

    /**
     * @return true if this listener is was not called
     */
    public boolean isNotCalled();
}
