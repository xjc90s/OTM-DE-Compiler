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
import org.opentravel.ns.ota2.librarymodel_v01_06.EnumValue;
import org.opentravel.ns.ota2.librarymodel_v01_06.EnumerationClosed;
import org.opentravel.ns.ota2.librarymodel_v01_06.Extension;
import org.opentravel.schemacompiler.model.TLClosedEnumeration;
import org.opentravel.schemacompiler.model.TLDocumentation;
import org.opentravel.schemacompiler.model.TLEnumValue;
import org.opentravel.schemacompiler.model.TLExtension;
import org.opentravel.schemacompiler.transform.ObjectTransformer;
import org.opentravel.schemacompiler.transform.symbols.SymbolResolverTransformerContext;
import org.opentravel.schemacompiler.transform.util.BaseTransformer;

/**
 * Handles the transformation of objects from the <code>TLClosedEnumeration</code> type to the
 * <code>EnumerationClosed</code> type.
 * 
 * @author S. Livezey
 */
public class TLClosedEnumerationTransformer
    extends BaseTransformer<TLClosedEnumeration,EnumerationClosed,SymbolResolverTransformerContext> {

    @Override
    public EnumerationClosed transform(TLClosedEnumeration source) {
        ObjectTransformer<TLExtension,Extension,SymbolResolverTransformerContext> extTransformer =
            getTransformerFactory().getTransformer( TLExtension.class, Extension.class );
        ObjectTransformer<TLDocumentation,Documentation,SymbolResolverTransformerContext> docTransformer =
            getTransformerFactory().getTransformer( TLDocumentation.class, Documentation.class );
        ObjectTransformer<TLEnumValue,EnumValue,SymbolResolverTransformerContext> valueTransformer =
            getTransformerFactory().getTransformer( TLEnumValue.class, EnumValue.class );
        EnumerationClosed enumType = new EnumerationClosed();

        enumType.setName( trimString( source.getName(), false ) );

        if (source.getExtension() != null) {
            enumType.setExtension( extTransformer.transform( source.getExtension() ) );
        }

        if ((source.getDocumentation() != null) && !source.getDocumentation().isEmpty()) {
            enumType.setDocumentation( docTransformer.transform( source.getDocumentation() ) );
        }

        for (TLEnumValue modelValue : source.getValues()) {
            enumType.getValue().add( valueTransformer.transform( modelValue ) );
        }
        return enumType;
    }

}
