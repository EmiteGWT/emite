/*
 * ((e)) emite: A pure Google Web Toolkit XMPP library
 * Copyright (c) 2008-2011 The Emite development team
 * 
 * This file is part of Emite.
 *
 * Emite is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * Emite is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with Emite.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * Emite Core.
 * <p>
 * The Emite Core module implements the Extensible Messaging and Presence
 * Protocol (XMPP): Core
 * <p>
 * The Extensible Messaging and Presence Protocol (XMPP) is an open Extensible
 * Markup Language XML [XML] protocol for near-real-time messaging, presence,
 * and request-response services. The basic syntax and semantics were developed
 * originally within the Jabber open-source community, mainly in 1999.
 * <p>
 * The core features -- mainly XML streams, use of TLS and SASL, and the
 * {@code <message/>}, {@code <presence/>}, and {@code <iq/>} children of the
 * stream root -- provide the building blocks for many types of near-real-time
 * applications, which may be layered on top of the core by sending
 * application-specific data qualified by particular XML namespaces [XML-NAMES]
 * 
 * @see <a href="http://xmpp.org/rfcs/rfc6120.html">RFC 6120</a>
 */
@ParametersAreNonnullByDefault
package com.calclab.emite.core;

import javax.annotation.ParametersAreNonnullByDefault;