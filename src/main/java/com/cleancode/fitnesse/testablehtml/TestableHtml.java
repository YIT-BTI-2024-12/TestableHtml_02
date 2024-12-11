package com.cleancode.fitnesse.testablehtml;

import fitnesse.responders.run.SuiteResponder;
import fitnesse.wiki.*;

public class TestableHtml {

    public static final String INCLUDE_SETUP = "!include -setup .";
    public static final String INCLUDE_TEARDOWN = "!include -teardown .";
    public static final String TEST = "Test";
    public static final String SET_UP = "SetUp";
    public static final String TEAR_DOWN = "TearDown";

    public String testableHtml(PageData pageData, boolean includeSuiteSetup) throws Exception {
        WikiPage wikiPage = pageData.getWikiPage();
        StringBuffer contentBuffer = new StringBuffer();

        boolean checkPageData = pageData.hasAttribute(TEST);

        if (checkPageData) {
            if (includeSuiteSetup) {
                renderPage(wikiPage, contentBuffer, INCLUDE_SETUP, SuiteResponder.SUITE_SETUP_NAME);
            }
            renderPage(wikiPage, contentBuffer, INCLUDE_SETUP, SET_UP);
        }

        contentBuffer.append(pageData.getContent());

        if (checkPageData) {
            renderPage(wikiPage, contentBuffer, INCLUDE_TEARDOWN, TEAR_DOWN);

            if (includeSuiteSetup) {
                renderPage(wikiPage, contentBuffer, INCLUDE_TEARDOWN, SuiteResponder.SUITE_TEARDOWN_NAME);
            }
        }

        pageData.setContent(contentBuffer.toString());
        return pageData.getHtml();
    }

    private void renderPage(WikiPage wikiPage, StringBuffer contentBuffer, String include, String suite) throws Exception {
        WikiPage page = PageCrawlerImpl.getInheritedPage(suite, wikiPage);

        if(page != null) {
            WikiPagePath pagePath = wikiPage.getPageCrawler().getFullPath(page);
            String pagePathName = PathParser.render(pagePath);
            contentBuffer.append(include).append(pagePathName).append("\n");
        }
    }
}
