/**
 * Copyright (C) 2014 OpenTravel Alliance (info@opentravel.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opentravel.schemacompiler.transform.tl2jaxb16;

import org.opentravel.ns.ota2.librarymodel_v01_06.Documentation;
import org.opentravel.ns.ota2.librarymodel_v01_06.DocumentationPatch;
import org.opentravel.schemacompiler.model.TLDocumentation;
import org.opentravel.schemacompiler.model.TLDocumentationPatch;
import org.opentravel.schemacompiler.transform.ObjectTransformer;
import org.opentravel.schemacompiler.transform.symbols.SymbolResolverTransformerContext;
import org.opentravel.schemacompiler.transform.util.BaseTransformer;

/**
 * Handles the transformation of objects from the <code>TLDocumentationPatch</code> type to the
 * <code>DocumentationPatch</code> type.
 *
 * @author S. Livezey
 */
public class TLDocumentationPatchTransformer
    extends BaseTransformer<TLDocumentationPatch,DocumentationPatch,SymbolResolverTransformerContext> {

    /**
     * @see org.opentravel.schemacompiler.transform.ObjectTransformer#transform(java.lang.Object)
     */
    @Override
    public DocumentationPatch transform(TLDocumentationPatch source) {
        DocumentationPatch target = new DocumentationPatch();

        target.setPatchedVersion( source.getPatchedVersion() );
        target.setDocPath( source.getDocPath() );

        if ((source.getDocumentation() != null) && !source.getDocumentation().isEmpty()) {
            ObjectTransformer<TLDocumentation,Documentation,SymbolResolverTransformerContext> docTransformer =
                getTransformerFactory().getTransformer( TLDocumentation.class, Documentation.class );

            target.setDocumentation( docTransformer.transform( source.getDocumentation() ) );
        }
        return target;
    }

}
