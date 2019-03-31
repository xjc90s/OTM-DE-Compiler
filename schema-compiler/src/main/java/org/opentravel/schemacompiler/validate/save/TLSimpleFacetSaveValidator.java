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

package org.opentravel.schemacompiler.validate.save;

import org.opentravel.schemacompiler.model.TLSimpleFacet;
import org.opentravel.schemacompiler.validate.FindingType;
import org.opentravel.schemacompiler.validate.ValidationFindings;
import org.opentravel.schemacompiler.validate.base.TLSimpleFacetBaseValidator;
import org.opentravel.schemacompiler.validate.impl.TLValidationBuilder;

/**
 * Validator for the <code>TLSimpleFacet</code> class.
 * 
 * 
 * @author S. Livezey
 */
public class TLSimpleFacetSaveValidator extends TLSimpleFacetBaseValidator {

    /**
     * @see org.opentravel.schemacompiler.validate.impl.TLValidatorBase#validateFields(org.opentravel.schemacompiler.validate.Validatable)
     */
    @Override
    protected ValidationFindings validateFields(TLSimpleFacet target) {
        TLValidationBuilder builder = newValidationBuilder( target );

        builder.setProperty( "equivalents", target.getEquivalents() ).setFindingType( FindingType.WARNING )
            .assertNotNull().assertContainsNoNullElements();

        return builder.getFindings();
    }

}
