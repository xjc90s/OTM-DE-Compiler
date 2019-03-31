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

package org.opentravel.schemacompiler.codegen.html.markup;


import org.opentravel.schemacompiler.codegen.html.Content;
import org.opentravel.schemacompiler.codegen.html.DocletConstants;
import org.opentravel.schemacompiler.codegen.html.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Class for generating HTML tree for javadoc output.
 *
 * @author Bhavesh Patel
 */
public class HtmlTree extends Content {

    private HtmlTag htmlTag;
    private Map<String,String> attrs = Collections.<String, String>emptyMap();
    private List<Content> content = Collections.<Content>emptyList();
    public static final Content EMPTY = new StringContent( "" );

    /**
     * Constructor to construct HtmlTree object.
     *
     * @param tag HTML tag for the HtmlTree object
     */
    public HtmlTree(HtmlTag tag) {
        htmlTag = nullCheck( tag );
    }

    /**
     * Constructor to construct HtmlTree object.
     *
     * @param tag HTML tag for the HtmlTree object
     * @param contents contents to be added to the tree
     */
    public HtmlTree(HtmlTag tag, Content... contents) {
        this( tag );

        for (Content c : contents) {
            addContent( c );
        }
    }

    /**
     * Adds an attribute for the HTML tag.
     *
     * @param attrName name of the attribute
     * @param attrValue value of the attribute
     */
    public void addAttr(HtmlAttr attrName, String attrValue) {
        if (attrs.isEmpty()) {
            attrs = new LinkedHashMap<>();
        }
        attrs.put( nullCheck( attrName.toString() ), nullCheck( attrValue ) );
    }

    /**
     * Adds an attribute for the HTML tag.
     *
     * @param name name of the attribute
     * @param attrValue value of the attribute
     */
    public void addDataAttr(String name, String attrValue) {
        if (attrs.isEmpty()) {
            attrs = new LinkedHashMap<>();
        }
        attrs.put( "data-" + nullCheck( name ), nullCheck( attrValue ) );
    }

    /**
     * Adds a style for the HTML tag. Overwrites any previous style
     *
     * @param style style to be added
     */
    public void setStyle(HtmlStyle style) {
        addAttr( HtmlAttr.CLASS, style.toString() );
    }

    /**
     * Adds a style for the HTML tag.
     *
     * @param style style to be added
     */
    public void addStyle(HtmlStyle style) {
        if (hasAttr( HtmlAttr.CLASS )) {
            String c = attrs.get( HtmlAttr.CLASS.toString() );

            if (!c.contains( style.toString() )) {
                c = c + " " + style;
                attrs.put( HtmlAttr.CLASS.toString(), c );
            }
        } else {
            setStyle( style );
        }
    }

    /**
     * Adds content for the HTML tag.
     *
     * @param tagContent tag content to be added
     */
    public void addContent(Content tagContent) {
        if (tagContent == HtmlTree.EMPTY || tagContent.isValid()) {
            if (content.isEmpty()) {
                content = new ArrayList<>();
            }
            content.add( tagContent );
        }
    }

    /**
     * This method adds a string content to the htmltree. If the last content member added is a StringContent, append
     * the string to that StringContent or else create a new StringContent and add it to the html tree.
     *
     * @param stringContent string content that needs to be added
     */
    public void addContent(String stringContent) {
        if (!content.isEmpty()) {
            Content lastContent = content.get( content.size() - 1 );

            if (lastContent instanceof StringContent) {
                lastContent.addContent( stringContent );
            } else {
                addContent( new StringContent( stringContent ) );
            }
        } else {
            addContent( new StringContent( stringContent ) );
        }
    }

    /**
     * Generates an HTML anchor tag.
     *
     * @param ref reference url for the anchor tag
     * @param body content for the anchor tag
     * @return an HtmlTree object
     */
    public static HtmlTree anchor(String ref, Content body) {
        HtmlTree htmltree = new HtmlTree( HtmlTag.A, nullCheck( body ) );

        htmltree.addAttr( HtmlAttr.HREF, nullCheck( ref ) );
        return htmltree;
    }

    /**
     * Generates an HTML anchor tag with name attribute and content.
     *
     * @param name name for the anchor tag
     * @param body content for the anchor tag
     * @return an HtmlTree object
     */
    public static HtmlTree anchorName(String name, Content body) {
        HtmlTree htmltree = HtmlTree.anchorName( name );
        htmltree.addContent( nullCheck( body ) );
        return htmltree;
    }

    /**
     * Generates an HTML anchor tag with name attribute.
     *
     * @param name name for the anchor tag
     * @return an HtmlTree object
     */
    public static HtmlTree anchorName(String name) {
        HtmlTree htmltree = new HtmlTree( HtmlTag.A );
        htmltree.addAttr( HtmlAttr.NAME, nullCheck( name ) );
        return htmltree;
    }

    /**
     * Generates a CAPTION tag with some content.
     *
     * @param body content for the tag
     * @return an HtmlTree object for the CAPTION tag
     */
    public static HtmlTree caption(Content body) {
        return new HtmlTree( HtmlTag.CAPTION, nullCheck( body ) );
    }

    /**
     * Generates a CODE tag with some content.
     *
     * @param body content for the tag
     * @return an HtmlTree object for the CODE tag
     */
    public static HtmlTree code(Content body) {
        return new HtmlTree( HtmlTag.CODE, nullCheck( body ) );
    }

    /**
     * Generates a DD tag with some content.
     *
     * @param body content for the tag
     * @return an HtmlTree object for the DD tag
     */
    public static HtmlTree dd(Content body) {
        return new HtmlTree( HtmlTag.DD, nullCheck( body ) );
    }

    /**
     * Generates a DL tag with some content.
     *
     * @param body content for the tag
     * @return an HtmlTree object for the DL tag
     */
    public static HtmlTree dl(Content body) {
        return new HtmlTree( HtmlTag.DL, nullCheck( body ) );
    }

    /**
     * Generates a DIV tag with the style class attributes. It also encloses a content.
     *
     * @param styleClass stylesheet class for the tag
     * @param body content for the tag
     * @return an HtmlTree object for the DIV tag
     */
    public static HtmlTree div(HtmlStyle styleClass, Content body) {
        HtmlTree htmltree = new HtmlTree( HtmlTag.DIV, nullCheck( body ) );

        if (styleClass != null) {
            htmltree.setStyle( styleClass );
        }
        return htmltree;
    }

    /**
     * Generates a DIV tag with some content.
     *
     * @param body content for the tag
     * @return an HtmlTree object for the DIV tag
     */
    public static HtmlTree div(Content body) {
        return div( null, body );
    }

    /**
     * Generates a DT tag with some content.
     *
     * @param body content for the tag
     * @return an HtmlTree object for the DT tag
     */
    public static HtmlTree dt(Content body) {
        return new HtmlTree( HtmlTag.DT, nullCheck( body ) );
    }

    /**
     * Generates a EM tag with some content.
     *
     * @param body content to be added to the tag
     * @return an HtmlTree object for the EM tag
     */
    public static HtmlTree em(Content body) {
        return new HtmlTree( HtmlTag.EM, nullCheck( body ) );
    }

    /**
     * Generates a FRAME tag.
     *
     * @param src the url of the document to be shown in the frame
     * @param name specifies the name of the frame
     * @param title the TITLE for the frame
     * @param scrolling specifies whether to display scrollbars in the frame
     * @return an HtmlTree object for the FRAME tag
     */
    public static HtmlTree frame(String src, String name, String title, String scrolling) {
        HtmlTree htmltree = new HtmlTree( HtmlTag.FRAME );

        htmltree.addAttr( HtmlAttr.SRC, nullCheck( src ) );
        htmltree.addAttr( HtmlAttr.NAME, nullCheck( name ) );
        htmltree.addAttr( HtmlAttr.TITLE, nullCheck( title ) );

        if (scrolling != null) {
            htmltree.addAttr( HtmlAttr.SCROLLING, scrolling );
        }
        return htmltree;
    }

    /**
     * Generates a Frame tag.
     *
     * @param src the url of the document to be shown in the frame
     * @param name specifies the name of the frame
     * @param title the title for the frame
     * @return an HtmlTree object for the SPAN tag
     */
    public static HtmlTree frame(String src, String name, String title) {
        return frame( src, name, title, null );
    }

    /**
     * Generates a FRAMESET tag.
     *
     * @param cols the size of columns in the frameset
     * @param rows the size of rows in the frameset
     * @param title the title for the frameset
     * @param onload the script to run when the document loads
     * @return an HtmlTree object for the FRAMESET tag
     */
    public static HtmlTree frameset(String cols, String rows, String title, String onload) {
        HtmlTree htmltree = new HtmlTree( HtmlTag.FRAMESET );

        if (cols != null) {
            htmltree.addAttr( HtmlAttr.COLS, cols );
        }
        if (rows != null) {
            htmltree.addAttr( HtmlAttr.ROWS, rows );
        }
        htmltree.addAttr( HtmlAttr.TITLE, nullCheck( title ) );
        htmltree.addAttr( HtmlAttr.ONLOAD, nullCheck( onload ) );
        return htmltree;
    }

    /**
     * Generates a heading tag (h1 to h6) with the title and style class attributes. It also encloses a content.
     *
     * @param headingTag the heading tag to be generated
     * @param printTitle true if title for the tag needs to be printed else false
     * @param styleClass stylesheet class for the tag
     * @param body content for the tag
     * @return an HtmlTree object for the tag
     */
    public static HtmlTree heading(HtmlTag headingTag, boolean printTitle, HtmlStyle styleClass, Content body) {
        HtmlTree htmltree = new HtmlTree( headingTag, nullCheck( body ) );

        if (printTitle) {
            htmltree.addAttr( HtmlAttr.TITLE, Util.stripHtml( body.toString() ) );
        }
        if (styleClass != null) {
            htmltree.setStyle( styleClass );
        }
        return htmltree;
    }

    /**
     * Generates a heading tag (h1 to h6) with style class attribute. It also encloses a content.
     *
     * @param headingTag the heading tag to be generated
     * @param styleClass stylesheet class for the tag
     * @param body content for the tag
     * @return an HtmlTree object for the tag
     */
    public static HtmlTree heading(HtmlTag headingTag, HtmlStyle styleClass, Content body) {
        return heading( headingTag, false, styleClass, body );
    }

    /**
     * Generates a heading tag (h1 to h6) with the title attribute. It also encloses a content.
     *
     * @param headingTag the heading tag to be generated
     * @param printTitle true if the title for the tag needs to be printed else false
     * @param body content for the tag
     * @return an HtmlTree object for the tag
     */
    public static HtmlTree heading(HtmlTag headingTag, boolean printTitle, Content body) {
        return heading( headingTag, printTitle, null, body );
    }

    /**
     * Generates a heading tag (h1 to h6) with some content.
     *
     * @param headingTag the heading tag to be generated
     * @param body content for the tag
     * @return an HtmlTree object for the tag
     */
    public static HtmlTree heading(HtmlTag headingTag, Content body) {
        return heading( headingTag, false, null, body );
    }

    /**
     * Generates an HTML tag with lang attribute. It also adds head and body content to the HTML tree.
     *
     * @param lang language for the HTML document
     * @param head head for the HTML tag
     * @param body body for the HTML tag
     * @return an HtmlTree object for the HTML tag
     */
    public static HtmlTree html(String lang, Content head, Content body) {
        HtmlTree htmltree = new HtmlTree( HtmlTag.HTML, nullCheck( head ), nullCheck( body ) );

        htmltree.addAttr( HtmlAttr.LANG, nullCheck( lang ) );
        return htmltree;
    }

    /**
     * Generates a I tag with some content.
     *
     * @param body content for the tag
     * @return an HtmlTree object for the I tag
     */
    public static HtmlTree italic(Content body) {
        return new HtmlTree( HtmlTag.I, nullCheck( body ) );
    }

    /**
     * Generates a LI tag with some content.
     *
     * @param body content for the tag
     * @return an HtmlTree object for the LI tag
     */
    public static HtmlTree li(Content body) {
        return li( null, body );
    }

    /**
     * Generates a LI tag with some content.
     *
     * @param styleClass style for the tag
     * @param body content for the tag
     * @return an HtmlTree object for the LI tag
     */
    public static HtmlTree li(HtmlStyle styleClass, Content body) {
        HtmlTree htmltree = new HtmlTree( HtmlTag.LI, nullCheck( body ) );

        if (styleClass != null) {
            htmltree.setStyle( styleClass );
        }
        return htmltree;
    }

    /**
     * Generates a LINK tag with the rel, type, href and title attributes.
     *
     * @param rel relevance of the link
     * @param type type of link
     * @param href the path for the link
     * @param title title for the link
     * @return an HtmlTree object for the LINK tag
     */
    public static HtmlTree link(String rel, String type, String href, String title) {
        HtmlTree htmltree = new HtmlTree( HtmlTag.LINK );
        htmltree.addAttr( HtmlAttr.REL, nullCheck( rel ) );
        htmltree.addAttr( HtmlAttr.TYPE, nullCheck( type ) );
        htmltree.addAttr( HtmlAttr.HREF, nullCheck( href ) );
        htmltree.addAttr( HtmlAttr.TITLE, nullCheck( title ) );
        return htmltree;
    }

    /**
     * Generates a META tag with the http-equiv, content and charset attributes.
     *
     * @param httpEquiv http equiv attribute for the META tag
     * @param content type of content
     * @param charSet character set used
     * @return an HtmlTree object for the META tag
     */
    public static HtmlTree meta(String httpEquiv, String content, String charSet) {
        HtmlTree htmltree = new HtmlTree( HtmlTag.META );
        htmltree.addAttr( HtmlAttr.HTTP_EQUIV, nullCheck( httpEquiv ) );
        htmltree.addAttr( HtmlAttr.CONTENT, nullCheck( content ) );
        htmltree.addAttr( HtmlAttr.CHARSET, nullCheck( charSet ) );
        return htmltree;
    }

    /**
     * Generates a META tag with the name and content attributes.
     *
     * @param name name attribute
     * @param content type of content
     * @return an HtmlTree object for the META tag
     */
    public static HtmlTree meta(String name, String content) {
        HtmlTree htmltree = new HtmlTree( HtmlTag.META );
        htmltree.addAttr( HtmlAttr.NAME, nullCheck( name ) );
        htmltree.addAttr( HtmlAttr.CONTENT, nullCheck( content ) );
        return htmltree;
    }

    /**
     * Generates a NOSCRIPT tag with some content.
     *
     * @param body content of the noscript tag
     * @return an HtmlTree object for the NOSCRIPT tag
     */
    public static HtmlTree noscript(Content body) {
        return new HtmlTree( HtmlTag.NOSCRIPT, nullCheck( body ) );
    }

    /**
     * Generates a P tag with some content.
     *
     * @param body content of the Paragraph tag
     * @return an HtmlTree object for the P tag
     */
    public static HtmlTree paragraph(Content body) {
        return paragraph( null, body );
    }

    /**
     * Generates a P tag with some content.
     *
     * @param styleClass style of the Paragraph tag
     * @param body content of the Paragraph tag
     * @return an HtmlTree object for the P tag
     */
    public static HtmlTree paragraph(HtmlStyle styleClass, Content body) {
        HtmlTree htmltree = new HtmlTree( HtmlTag.P, nullCheck( body ) );

        if (styleClass != null) {
            htmltree.setStyle( styleClass );
        }
        return htmltree;
    }

    /**
     * Generates a SMALL tag with some content.
     *
     * @param body content for the tag
     * @return an HtmlTree object for the SMALL tag
     */
    public static HtmlTree small(Content body) {
        return new HtmlTree( HtmlTag.SMALL, nullCheck( body ) );
    }

    /**
     * Generates a STRONG tag with some content.
     *
     * @param body content for the tag
     * @return an HtmlTree object for the STRONG tag
     */
    public static HtmlTree strong(Content body) {
        return new HtmlTree( HtmlTag.STRONG, nullCheck( body ) );
    }

    /**
     * Generates a SPAN tag with some content.
     *
     * @param body content for the tag
     * @return an HtmlTree object for the SPAN tag
     */
    public static HtmlTree span(Content body) {
        return span( null, body );
    }

    /**
     * Generates a SPAN tag with style class attribute and some content.
     *
     * @param styleClass style class for the tag
     * @param body content for the tag
     * @return an HtmlTree object for the SPAN tag
     */
    public static HtmlTree span(HtmlStyle styleClass, Content body) {
        HtmlTree htmltree = new HtmlTree( HtmlTag.SPAN, nullCheck( body ) );

        if (styleClass != null) {
            htmltree.setStyle( styleClass );
        }
        return htmltree;
    }

    /**
     * Generates a Table tag with border, width and summary attributes and some content.
     *
     * @param border border for the table
     * @param width width of the table
     * @param summary summary for the table
     * @param body content for the table
     * @return an HtmlTree object for the TABLE tag
     */
    public static HtmlTree table(int border, int width, String summary, Content body) {
        HtmlTree htmltree = new HtmlTree( HtmlTag.TABLE, nullCheck( body ) );

        htmltree.addAttr( HtmlAttr.BORDER, Integer.toString( border ) );
        htmltree.addAttr( HtmlAttr.WIDTH, Integer.toString( width ) );
        htmltree.addAttr( HtmlAttr.SUMMARY, nullCheck( summary ) );
        return htmltree;
    }

    /**
     * Generates a Table tag with style class, border, cell padding, cellspacing and summary attributes and some
     * content.
     *
     * @param styleClass style of the table
     * @param border border for the table
     * @param cellPadding cell padding for the table
     * @param cellSpacing cell spacing for the table
     * @param summary summary for the table
     * @param body content for the table
     * @return an HtmlTree object for the TABLE tag
     */
    public static HtmlTree table(HtmlStyle styleClass, int border, int cellPadding, int cellSpacing, String summary,
        Content body) {
        HtmlTree htmltree = new HtmlTree( HtmlTag.TABLE, nullCheck( body ) );

        if (styleClass != null) {
            htmltree.setStyle( styleClass );
        }
        htmltree.addAttr( HtmlAttr.BORDER, Integer.toString( border ) );
        htmltree.addAttr( HtmlAttr.CELLPADDING, Integer.toString( cellPadding ) );
        htmltree.addAttr( HtmlAttr.CELLSPACING, Integer.toString( cellSpacing ) );
        htmltree.addAttr( HtmlAttr.SUMMARY, nullCheck( summary ) );
        return htmltree;
    }

    /**
     * Generates a Table tag with border, cell padding, cellspacing and summary attributes and some content.
     *
     * @param border border for the table
     * @param cellPadding cell padding for the table
     * @param cellSpacing cell spacing for the table
     * @param summary summary for the table
     * @param body content for the table
     * @return an HtmlTree object for the TABLE tag
     */
    public static HtmlTree table(int border, int cellPadding, int cellSpacing, String summary, Content body) {
        return table( null, border, cellPadding, cellSpacing, summary, body );
    }

    /**
     * Generates a TD tag with style class attribute and some content.
     *
     * @param styleClass style for the tag
     * @param body content for the tag
     * @return an HtmlTree object for the TD tag
     */
    public static HtmlTree td(HtmlStyle styleClass, Content body) {
        HtmlTree htmltree = new HtmlTree( HtmlTag.TD, nullCheck( body ) );

        if (styleClass != null) {
            htmltree.setStyle( styleClass );
        }
        return htmltree;
    }

    /**
     * Generates a TD tag for an HTML table with some content.
     *
     * @param body content for the tag
     * @return an HtmlTree object for the TD tag
     */
    public static HtmlTree td(Content body) {
        return td( null, body );
    }

    /**
     * Generates a TH tag with style class and scope attributes and some content.
     *
     * @param styleClass style for the tag
     * @param scope scope of the tag
     * @param body content for the tag
     * @return an HtmlTree object for the TH tag
     */
    public static HtmlTree th(HtmlStyle styleClass, String scope, Content body) {
        HtmlTree htmltree = new HtmlTree( HtmlTag.TH, nullCheck( body ) );

        if (styleClass != null) {
            htmltree.setStyle( styleClass );
        }
        htmltree.addAttr( HtmlAttr.SCOPE, nullCheck( scope ) );
        return htmltree;
    }

    /**
     * Generates a TH tag with scope attribute and some content.
     *
     * @param scope scope of the tag
     * @param body content for the tag
     * @return an HtmlTree object for the TH tag
     */
    public static HtmlTree th(String scope, Content body) {
        return th( null, scope, body );
    }

    /**
     * Generates a title tag with some content.
     *
     * @param body content for the tag
     * @return an HtmlTree object for the title tag
     */
    public static HtmlTree title(Content body) {
        return new HtmlTree( HtmlTag.TITLE, nullCheck( body ) );
    }

    /**
     * Generates a TR tag for an HTML table with some content.
     *
     * @param body content for the tag
     * @return an HtmlTree object for the TR tag
     */
    public static HtmlTree tr(Content body) {
        return new HtmlTree( HtmlTag.TR, nullCheck( body ) );
    }

    /**
     * Generates a UL tag with the style class attribute and some content.
     *
     * @param styleClass style for the tag
     * @param body content for the tag
     * @return an HtmlTree object for the UL tag
     */
    public static HtmlTree ul(HtmlStyle styleClass, Content body) {
        HtmlTree htmltree = new HtmlTree( HtmlTag.UL, nullCheck( body ) );

        htmltree.setStyle( nullCheck( styleClass ) );
        return htmltree;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isEmpty() {
        return (!hasContent() && !hasAttrs());
    }

    /**
     * Returns true if the HTML tree has content.
     *
     * @return true if the HTML tree has content else return false
     */
    public boolean hasContent() {
        return (!content.isEmpty());
    }

    /**
     * Returns true if the HTML tree has attributes.
     *
     * @return true if the HTML tree has attributes else return false
     */
    public boolean hasAttrs() {
        return (!attrs.isEmpty());
    }

    /**
     * Returns true if the HTML tree has a specific attribute.
     *
     * @param attrName name of the attribute to check within the HTML tree
     * @return true if the HTML tree has the specified attribute else return false
     */
    public boolean hasAttr(HtmlAttr attrName) {
        return (attrs.containsKey( attrName.toString() ));
    }

    /**
     * Returns true if the HTML tree is valid. This check is more specific to standard doclet and not exactly similar to
     * W3C specifications. But it ensures HTML validation.
     *
     * @return true if the HTML tree is valid
     */
    @Override
    public boolean isValid() {
        switch (htmlTag) {
            case A:
                return (hasAttr( HtmlAttr.NAME ) || (hasAttr( HtmlAttr.HREF ) && hasContent()));
            case BR:
                return (!hasContent() && (!hasAttrs() || hasAttr( HtmlAttr.CLEAR )));
            case FRAME:
                return (hasAttr( HtmlAttr.SRC ) && !hasContent());
            case HR:
                return (!hasContent());
            case IMG:
                return (hasAttr( HtmlAttr.SRC ) && hasAttr( HtmlAttr.ALT ) && !hasContent());
            case LINK:
                return (hasAttr( HtmlAttr.HREF ) && !hasContent());
            case META:
                return (hasAttr( HtmlAttr.CONTENT ) && !hasContent());
            case SCRIPT:
                return (hasAttr( HtmlAttr.SRC ) || hasContent());
            default:
                return hasContent();
        }
    }

    /**
     * Returns true if the element is an inline element.
     *
     * @return true if the HTML tag is an inline element
     */
    public boolean isInline() {
        return (htmlTag.blockType == HtmlTag.BlockType.INLINE);
    }

    /**
     * {@inheritDoc}
     */
    public void write(StringBuilder contentBuilder) {
        if (!isInline() && !endsWithNewLine( contentBuilder )) {
            contentBuilder.append( DocletConstants.NL );
        }
        String tagString = htmlTag.toString();

        contentBuilder.append( "<" );
        contentBuilder.append( tagString );

        Iterator<String> iterator = attrs.keySet().iterator();
        String key;
        String value = "";

        while (iterator.hasNext()) {
            key = iterator.next();
            value = attrs.get( key );
            contentBuilder.append( " " );
            contentBuilder.append( key );

            if (!value.isEmpty()) {
                contentBuilder.append( "=\"" );
                contentBuilder.append( value );
                contentBuilder.append( "\"" );
            }
        }
        contentBuilder.append( ">" );

        for (Content c : content) {
            c.write( contentBuilder );
        }
        if (htmlTag.endTagRequired()) {
            contentBuilder.append( "</" );
            contentBuilder.append( tagString );
            contentBuilder.append( ">" );
        }
        if (!isInline()) {
            contentBuilder.append( DocletConstants.NL );
        }
    }
}
