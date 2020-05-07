/**
 *  Copyright (c) 2018 Angelo ZERR
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v2.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 */
package org.eclipse.lemminx.extensions.contentmodel.participants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.extensions.xsd.participants.XSDErrorCode;
import org.eclipse.lemminx.services.extensions.ICodeActionParticipant;
import org.eclipse.lemminx.services.extensions.IComponentProvider;
import org.eclipse.lemminx.settings.SharedSettings;
import org.eclipse.lsp4j.CodeAction;
import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.lsp4j.Range;

/**
 * Extension to support XML code actions based on content model (XML Schema
 * completion, etc)
 */
public class ContentModelCodeActionParticipant implements ICodeActionParticipant {

	private final Map<String, ICodeActionParticipant> codeActionParticipants;

	public ContentModelCodeActionParticipant() {
		codeActionParticipants = new HashMap<>();
		XMLSyntaxErrorCode.registerCodeActionParticipants(codeActionParticipants);
		DTDErrorCode.registerCodeActionParticipants(codeActionParticipants);
		XMLSchemaErrorCode.registerCodeActionParticipants(codeActionParticipants);
		XSDErrorCode.registerCodeActionParticipants(codeActionParticipants);
	}

	@Override
	public void doCodeAction(Diagnostic diagnostic, Range range, DOMDocument document, List<CodeAction> codeActions,
			SharedSettings sharedSettings, IComponentProvider componentProvider) {
		if (!diagnostic.getCode().isLeft()) {
			return;
		}
		ICodeActionParticipant participant = codeActionParticipants.get(diagnostic.getCode().getLeft());
		if (participant != null) {
			participant.doCodeAction(diagnostic, range, document, codeActions, sharedSettings,
					componentProvider);
		}
	}
}
