/*
 * Copyright (c) 2018-2025 The Code Department.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.tcdng.unify.web.ui;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

import com.tcdng.unify.common.database.Entity;
import com.tcdng.unify.core.SessionContext;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.UserToken;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.Singleton;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.constant.MimeType;
import com.tcdng.unify.core.constant.TopicEventType;
import com.tcdng.unify.core.data.DownloadFile;
import com.tcdng.unify.core.data.FileAttachmentInfo;
import com.tcdng.unify.core.security.TwoWayStringCryptograph;
import com.tcdng.unify.core.task.TaskLauncher;
import com.tcdng.unify.core.task.TaskMonitor;
import com.tcdng.unify.core.task.TaskSetup;
import com.tcdng.unify.core.upl.UplElementReferences;
import com.tcdng.unify.core.util.ApplicationUtils;
import com.tcdng.unify.core.util.ReflectUtils;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.ClientCookie;
import com.tcdng.unify.web.ClientRequest;
import com.tcdng.unify.web.ClientResponse;
import com.tcdng.unify.web.ControllerPathParts;
import com.tcdng.unify.web.UnifyWebSessionAttributeConstants;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.constant.ClosePageMode;
import com.tcdng.unify.web.constant.ReadOnly;
import com.tcdng.unify.web.constant.ResetOnWrite;
import com.tcdng.unify.web.constant.ResultMappingConstants;
import com.tcdng.unify.web.constant.Secured;
import com.tcdng.unify.web.constant.UnifyWebRequestAttributeConstants;
import com.tcdng.unify.web.http.LongUserSessionManager;
import com.tcdng.unify.web.ui.widget.ContentPanel;
import com.tcdng.unify.web.ui.widget.DataTransferWidget;
import com.tcdng.unify.web.ui.widget.Document;
import com.tcdng.unify.web.ui.widget.Page;
import com.tcdng.unify.web.ui.widget.PageAction;
import com.tcdng.unify.web.ui.widget.ResponseWriter;
import com.tcdng.unify.web.ui.widget.Widget;
import com.tcdng.unify.web.ui.widget.WidgetCommandManager;
import com.tcdng.unify.web.ui.widget.WidgetContainer;
import com.tcdng.unify.web.ui.widget.data.DynPopup;
import com.tcdng.unify.web.ui.widget.data.Hint.MODE;
import com.tcdng.unify.web.ui.widget.data.MessageBox;
import com.tcdng.unify.web.ui.widget.data.MessageBoxCaptions;
import com.tcdng.unify.web.ui.widget.data.MessageIcon;
import com.tcdng.unify.web.ui.widget.data.MessageMode;
import com.tcdng.unify.web.ui.widget.data.MessageResult;
import com.tcdng.unify.web.ui.widget.data.Popup;
import com.tcdng.unify.web.ui.widget.data.TaskMonitorInfo;

/**
 * Convenient base page controller implementation.
 * 
 * @author The Code Department
 * @since 4.1
 */
@Singleton
@UplBinding("web/reserved/upl/basepage.upl")
public abstract class AbstractPageController<T extends PageBean> extends AbstractUIController
		implements PageController<T> {

	@Configurable
	private TaskLauncher taskLauncher;

	@Configurable
	private PagePathInfoRepository pagePathInfoRepository;

	@Configurable
	private WidgetCommandManager uiCommandManager;

	private Class<T> pageBeanClass;

	public AbstractPageController(Class<T> pageBeanClass) {
		this(pageBeanClass, Secured.FALSE, ReadOnly.FALSE, ResetOnWrite.FALSE);
	}

	public AbstractPageController(Class<T> pageBeanClass, Secured secured, ReadOnly readOnly,
			ResetOnWrite resetOnWrite) {
		super(secured, readOnly, resetOnWrite);
		this.pageBeanClass = pageBeanClass;
	}

	@Override
	public void ensureContextResources(ControllerPathParts controllerPathParts) throws UnifyException {
		SessionContext sessionContext = getSessionContext();
		final String pageId = getPageManager().getCurrentRequestPageId(controllerPathParts);
		if (sessionContext != null && sessionContext.getAttribute(pageId) == null) {
			Page page = getPageManager().createPage(sessionContext.getLocale(),
					controllerPathParts.getControllerName());
			page.setPathParts(controllerPathParts, pageId);
			Class<? extends PageBean> pageBeanClass = getPageBeanClass();
			if (VoidPageBean.class.equals(pageBeanClass)) {
				page.setPageBean(VoidPageBean.INSTANCE);
			} else {
				page.setPageBean(ReflectUtils.newInstance(pageBeanClass));
			}

			getPageRequestContextUtil().setRequestPage(page);
			initPage();
			sessionContext.setAttribute(pageId, page);
		}
	}

	@Override
	public void reset() throws UnifyException {
		getPageBean().reset();
	}

	@Override
	public final Class<T> getPageBeanClass() {
		return pageBeanClass;
	}

	@Override
	public final boolean isPageController() {
		return true;
	}

	@Override
	public final Page getPage() throws UnifyException {
		return getPageRequestContextUtil().getRequestPage();
	}

	@Override
	public final void initPage() throws UnifyException {
		onInitPage();
	}

	@Action
	@Override
	public final String reloadPage() throws UnifyException {
		onReloadPage();
		return ResultMappingConstants.RELOAD;
	}

	@Action
	public final String indexPage() throws UnifyException {
		onIndexPage();
		return getPageRequestContextUtil().isWithCommandResultMapping()
				? getPageRequestContextUtil().getCommandResultMapping()
				: ResultMappingConstants.INDEX;
	}

	@Action
	@Override
	public final String openPage() throws UnifyException {
		onOpenPage();
		onReloadPage();
		return getPageRequestContextUtil().isWithCommandResultMapping()
				? getPageRequestContextUtil().getCommandResultMapping()
				: ResultMappingConstants.OPEN;
	}

	@Action
	@Override
	public final String savePage() throws UnifyException {
		onSavePage();
		return getPageRequestContextUtil().isWithCommandResultMapping()
				? getPageRequestContextUtil().getCommandResultMapping()
				: ResultMappingConstants.SAVE;
	}

	@Action
	@Override
	public final String closePage() throws UnifyException {
		onClosePage();
		return getPageRequestContextUtil().isWithCommandResultMapping()
				? getPageRequestContextUtil().getCommandResultMapping()
				: ResultMappingConstants.CLOSE;
	}

	@Action
	public String noResult() throws UnifyException {
		return ResultMappingConstants.NONE;
	}

	@Action
	public String hidePopup() throws UnifyException {
		removeCurrentPopup();
		return ResultMappingConstants.HIDE_POPUP;
	}

	@Action
	public String hidePopupFireConfirm() throws UnifyException {
		return ResultMappingConstants.HIDE_POPUP_FIRE_CONFIRM;
	}

	@Action
	public String content() throws UnifyException {
		return ResultMappingConstants.NONE;
	}

	@Action
	public String command() throws UnifyException {
		RequestCommand requestCommand = getPageRequestContextUtil().getRequestCommand();
		if (requestCommand != null) {
			Widget widget = getPageRequestContextUtil().getRequestPage().getWidgetByLongName(Widget.class,
					requestCommand.getParentLongName());
			if (widget != null) {
				DataTransferBlock childBlock = requestCommand.getTransferBlock().getChildBlock();
				while (childBlock != null) {
					widget = ((WidgetContainer) widget).getChildWidget(childBlock.getId());
					childBlock = childBlock.getChildBlock();
				}

				if (widget.isRelayCommand()) {
					widget = widget.getRelayWidget();
				}

				uiCommandManager.executeCommand(widget, requestCommand.getCommand());
				String commandResultMapping = getPageRequestContextUtil().getCommandResultMapping();
				if (StringUtils.isNotBlank(commandResultMapping)) {
					return commandResultMapping;
				}
			}
		}

		return ResultMappingConstants.COMMAND;
	}

	@Action
	public String confirm() throws UnifyException {
		PageRequestContextUtil pageRequestContextUtil = getPageRequestContextUtil();
		String msg = pageRequestContextUtil.getRequestConfirmMessage();
		String param = pageRequestContextUtil.getRequestConfirmParam();
		if (StringUtils.isNotBlank(param)) {
			msg = MessageFormat.format(msg, param);
		}

		return showMessageBox(pageRequestContextUtil.getRequestConfirmMessageIcon(), MessageMode.YES_NO,
				getSessionMessage("messagebox.confirmation"), msg, "/confirmResult");
	}

	@Action
	public String confirmResult() throws UnifyException {
		if (MessageResult.YES.equals(getMessageResult())) {
			return hidePopupFireConfirm();
		}

		return hidePopup();
	}

	@Override
	public String executePageCall(String actionName) throws UnifyException {
		try {
			final PageRequestContextUtil ctxUtil = getPageRequestContextUtil();
			if ("/openPage".equals(actionName)) {
				ContentPanel contentPanel = ctxUtil.getRequestDocument() != null
						? ctxUtil.getRequestDocument().getContentPanel()
						: null;
				getPage().setAttribute(PageAttributeConstants.IN_DETACHED_WINDOW,
						contentPanel != null ? contentPanel.isDetachedWindow() : false);

				String resultName = openPage();
				if (contentPanel != null && isContentSupport()) {
					if (contentPanel.isBlankContent()
							&& !contentPanel.getPaths().contains(ctxUtil.getRequestPathParts().getControllerPath())) {
						for (String stickyPath : contentPanel.getPaths()) {
							fireOtherControllerAction(stickyPath);
						}
					}

					contentPanel.addContent(ctxUtil.getRequestPage());
				}

				return resultName;
			} else if ("/replacePage".equals(actionName)) {
				String resultName = openPage();
				ContentPanel contentPanel = ctxUtil.getRequestDocument().getContentPanel();
				Page currentPage = ctxUtil.getRequestPage();
				String pathToReplaceId = contentPanel.insertContent(currentPage);
				if (!StringUtils.isBlank(pathToReplaceId)) {
					performClosePages(contentPanel, Arrays.asList(pathToReplaceId), true);
				}

				return resultName;
			} else if ("/closePage".equals(actionName)) {
				ClosePageMode closePageMode = ctxUtil.getRequestTargetValue(ClosePageMode.class);
				performClosePage(closePageMode, true);
				return ResultMappingConstants.CLOSE;
			}

			String resultName = executeAction(this, actionName);
			if (ResultMappingConstants.CLOSE.equals(resultName)) {
				// Handle other actions that also return CLOSE result
				performClosePage(ClosePageMode.CLOSE, false);
			}

			return resultName;
		} catch (UnifyException e) {
			throw e;
		}
	}

	@Override
	public void populate(DataTransferBlock transferBlock) throws UnifyException {
		Page page = getPageRequestContextUtil().getRequestPage();
		DataTransferWidget dataTransferWidget = (DataTransferWidget) page
				.getWidgetByLongName(transferBlock.getLongName());
		if (dataTransferWidget != null) {
			dataTransferWidget.populate(transferBlock);
		}
	}

	/**
	 * Indicates if page controller supports addition to content panel
	 * 
	 * @return true if content panel is supported otherwise false
	 * @throws UnifyException if an error occurs
	 */
	protected boolean isContentSupport() throws UnifyException {
		return true;
	}

	@Override
	protected DataTransferParam getDataTransferParam() throws UnifyException {
		Page page = getPageRequestContextUtil().getRequestPage();
		return new DataTransferParam(getUIControllerUtil().getPageControllerInfo(getName()),
				(Class<?>) page.getAttribute("validationClass"), (Class<?>) page.getAttribute("validationIdClass"));
	}

	@Override
	protected void doProcess(ClientRequest request, ClientResponse response, PageController<?> docPageController,
			ControllerPathParts docPathParts) throws UnifyException {
		ResponseWriter writer = getResponseWriterPool().getResponseWriter(request);
		UIControllerUtil uiControllerUtil = getUIControllerUtil();
		final PageRequestContextUtil pageRequestContextUtil = getPageRequestContextUtil();
		pageRequestContextUtil.setClientRequest(request);
		pageRequestContextUtil.setClientResponse(response);
		try {
			final ControllerPathParts reqPathParts = request.getRequestPathParts().getControllerPathParts();
			pageRequestContextUtil.setRequestPathParts(reqPathParts);
			PageControllerInfo pbbInfo = uiControllerUtil.getPageControllerInfo(getName());
			ControllerPathParts respPathParts = reqPathParts;
			Page restPage = uiControllerUtil.loadRequestPage(reqPathParts);
			String resultName = ResultMappingConstants.VALIDATION_ERROR;

			DataTransfer dataTransfer = prepareDataTransfer(request);
			PageController<?> respPageController = null;
			if (validate(restPage, dataTransfer)) {
				synchronized (restPage) {
					populate(dataTransfer);
					if (reqPathParts.isActionPath()) {
						resultName = executePageCall(reqPathParts.getActionName());
					} else {
						resultName = executePageCall("/indexPage");
					}
				}

				if (StringUtils.isBlank(resultName)) {
					throwOperationErrorException(
							new IllegalArgumentException("No result name returned from action method call."));
				}

				respPageController = this;
				logDebug("Processing result with name [{0}]...", resultName);
				// Check if action result needs to be routed to containing
				// document controller
				if (!pbbInfo.hasResultWithName(resultName) && !restPage.isDocument()) {
					if (docPageController != null) {
						logDebug("Result with name [{0}] not found for controller [{1}]...", resultName,
								respPageController.getName());
						respPathParts = docPathParts;
						respPageController = docPageController;
						restPage = uiControllerUtil.loadRequestPage(respPathParts);
						pbbInfo = uiControllerUtil.getPageControllerInfo(respPageController.getName());
						logDebug("Result with name [{0}] routed to controller [{1}]...", resultName,
								respPageController.getName());
					}
				}

				// Route to common utilities if necessary
				if (!pbbInfo.hasResultWithName(resultName)) {
					logDebug("Result with name [{0}] not found for controller [{1}]...", resultName,
							respPageController.getName());
					respPathParts = pagePathInfoRepository
							.getControllerPathParts(uiControllerUtil.getCommonUtilitiesControllerName());
					respPageController = (PageController<?>) getControllerFinder().findController(respPathParts);
					restPage = uiControllerUtil.loadRequestPage(respPathParts);
					pbbInfo = uiControllerUtil.getPageControllerInfo(respPageController.getName());
					logDebug("Result with name [{0}] routed to controller [{1}]...", resultName,
							respPageController.getName());
				}
			}

			Result result = pbbInfo.getResult(resultName);
			if (result.isDocumentPathResponse()) {
				final String path = result.getDocumentPath();
				logDebug("Redirecting to load document controller [{0}]...", path);
				respPathParts = pagePathInfoRepository.getControllerPathParts(path);
				respPageController = (PageController<?>) getControllerFinder().findController(respPathParts);
				restPage = uiControllerUtil.loadRequestPage(respPathParts);
				executeAction(respPageController, "/indexPage");
			}

			// Write response using information from response path parts
			pageRequestContextUtil.setResponsePathParts(respPathParts);
			if (respPageController != null && result.isReload()) {
				respPageController.reloadPage();
			}

			writeResponse(writer, restPage, result);

			response.setContentType(
					result.isDocumentPathResponse() ? MimeType.TEXT_HTML.template() : result.getMimeType().template());
			if (request.getCharset() != null) {
				response.setCharacterEncoding(request.getCharset().name());
			}

			writer.writeTo(response.getWriter());
		} finally {
			// Remove closed pages from session
			getSessionContext().removeAttributes(pageRequestContextUtil.getClosedPagePaths());

			// Restore writer
			getResponseWriterPool().restore(writer);
		}
	}

	@SuppressWarnings("unchecked")
	protected T getPageBean() throws UnifyException {
		return (T) resolveRequestPage().getPageBean();
	}

	protected List<String> getPathVariables() throws UnifyException {
		return resolveRequestPage().getPathVariables();
	}

	/**
	 * Creates long user session.
	 * 
	 * @return true if set otherwise false
	 * @throws UnifyException if an error occurs
	 */
	protected boolean createLongUserSession() throws UnifyException {
		final ClientResponse response = getPageRequestContextUtil().getClientResponse();
		if (response != null) {
			if (isComponent(LongUserSessionManager.class)) {
				final LongUserSessionManager longUserSessionManager = getComponent(LongUserSessionManager.class);
				final TwoWayStringCryptograph cryptograph = getComponent(TwoWayStringCryptograph.class);
				final UserToken userToken = getUserToken();
				final int sessionInSecs = longUserSessionManager.getDefaultLongSessionSeconds();
				userToken.setSessionInSecs(sessionInSecs);
				final String cookieId = cryptograph.encrypt(ApplicationUtils.generateLongSessionCookieId(userToken));
				if (longUserSessionManager.saveLongSession(userToken.getUserLoginId(), cookieId)) {
					response.setCookie(longUserSessionManager.getLongSessionCookieName(), cookieId, sessionInSecs);
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Deletes long user session.
	 * 
	 * @param sessionInSecs session in seconds.
	 * @return true if set otherwise false
	 * @throws UnifyException if an error occurs
	 */
	protected boolean deleteLongUserSession() throws UnifyException {
		final ClientRequest request = getPageRequestContextUtil().getClientRequest();
		final ClientResponse response = getPageRequestContextUtil().getClientResponse();
		if (request != null && response != null) {
			if (isComponent(LongUserSessionManager.class)) {
				final LongUserSessionManager longUserSessionManager = getComponent(LongUserSessionManager.class);
				final String cookieName = longUserSessionManager.getLongSessionCookieName();
				ClientCookie clientCookie = request.getCookie(cookieName);
				if (clientCookie != null) {
					final String cookieId = clientCookie.getVal();
					longUserSessionManager.deleteLongSession(cookieId);
					response.setCookie(cookieName, cookieId, 0);
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Sets up an open path post response.
	 * 
	 * @param path the path to open
	 * @return {@link ResultMappingConstants#POST_RESPONSE}
	 * @throws UnifyException if an error occurs
	 */
	protected String openPath(String path) throws UnifyException {
		setRequestAttribute(UnifyWebRequestAttributeConstants.COMMAND_POSTRESPONSE_PATH, path);
		return ResultMappingConstants.POST_RESPONSE;
	}

	/**
	 * Sets up a file for download in current request context and returns a file
	 * download response.
	 * 
	 * @param downloadFile the file download object
	 * @return {@link ResultMappingConstants#DOWNLOAD_FILE}
	 * @throws UnifyException if an error occurs
	 */
	protected String fileDownloadResult(DownloadFile downloadFile) throws UnifyException {
		return fileDownloadResult(downloadFile, true);
	}

	/**
	 * Sets up a file for download in current request context and returns a file
	 * download response.
	 * 
	 * @param downloadFile the file download object
	 * @param hidePopup    hide popup flag
	 * @return {@link ResultMappingConstants#DOWNLOAD_FILE}
	 * @throws UnifyException if an error occurs
	 */
	protected String fileDownloadResult(DownloadFile downloadFile, boolean hidePopup) throws UnifyException {
		setRequestAttribute(UnifyWebRequestAttributeConstants.DOWNLOAD_FILE, downloadFile);
		if (hidePopup) {
			return ResultMappingConstants.DOWNLOAD_FILE_HIDE_POPUP;
		}

		return ResultMappingConstants.DOWNLOAD_FILE;
	}

	/**
	 * Shows a file attachment.
	 * 
	 * @param fileAttachmentInfo the file attachment information
	 * @return the result mapping
	 * @throws UnifyException if an error occurs
	 */
	protected String showAttachment(FileAttachmentInfo fileAttachmentInfo) throws UnifyException {
		setRequestAttribute(UnifyWebRequestAttributeConstants.FILEATTACHMENTS_INFO, fileAttachmentInfo);
		return ResultMappingConstants.SHOW_ATTACHMENT;
	}

	/**
	 * Shows popup.
	 * 
	 * @param popup the popup details
	 * @return the result mapping
	 * @throws UnifyException if an error occurs
	 */
	protected String showPopup(Popup popup) throws UnifyException {
		setSessionAttribute(UnifyWebSessionAttributeConstants.POPUP, popup);
		return popup.getResultMapping();
	}

	/**
	 * Shows a dynamic popup.
	 * 
	 * @param dynTitle     the title
	 * @param dynPanelName the panel name (should be a standalone panel)
	 * @param dynPanelBean the panel bean
	 * @param width        the popup width
	 * @param height       the popup height
	 * @return the result mapping
	 * @throws UnifyException if an error occurs
	 */
	protected String showDynamicPopup(String dynTitle, String dynPanelName, Object dynPanelBean, int width, int height)
			throws UnifyException {
		final Popup popup = new Popup(ResultMappingConstants.SHOW_DYNAMIC_POPUP,
				new DynPopup(dynTitle, dynPanelName, dynPanelBean), width, height);
		setSessionAttribute(UnifyWebSessionAttributeConstants.POPUP, popup);
		return popup.getResultMapping();
	}

	/**
	 * Gets current popup.
	 * 
	 * @return the current popup otherwise null
	 * @throws UnifyException if an error occurs
	 */
	protected Popup getCurrentPopup() throws UnifyException {
		return getSessionAttribute(Popup.class, UnifyWebSessionAttributeConstants.POPUP);
	}

	/**
	 * Gets current popup.
	 * 
	 * @return the current popup otherwise null
	 * @throws UnifyException if an error occurs
	 */
	protected Popup removeCurrentPopup() throws UnifyException {
		return (Popup) removeSessionAttribute(UnifyWebSessionAttributeConstants.POPUP);
	}

	/**
	 * Sets up a response that shows a message box with default info icon and OK
	 * button. {@link MessageBox} value of the session attribute
	 * {@link UnifyWebSessionAttributeConstants#MESSAGEBOX}.
	 * 
	 * @param message the message to display
	 * @return response to show application message box
	 * @throws UnifyException if an error occurs
	 */
	protected String showMessageBox(String message) throws UnifyException {
		return showMessageBox(MessageIcon.INFO, MessageMode.OK, "$m{messagebox.message}", message, "/hidePopup");
	}

	/**
	 * Sets up a response that shows a message box. The message box is backed by the
	 * {@link MessageBox} value of the session attribute
	 * {@link UnifyWebSessionAttributeConstants#MESSAGEBOX}.
	 * 
	 * @param messageIcon the message icon of enumeration type {@link MessageIcon}
	 * @param messageMode the message mode of enumeration type {@link MessageMode}
	 * @param message     the message to display
	 * @return response to show application message box
	 * @throws UnifyException if an error occurs
	 */
	protected String showMessageBox(MessageIcon messageIcon, MessageMode messageMode, String message)
			throws UnifyException {
		return showMessageBox(messageIcon, messageMode, "$m{messagebox.message}", message, "/hidePopup");
	}

	/**
	 * Sets up a response that shows a message box with default info icon and OK
	 * button. {@link MessageBox} value of the session attribute
	 * {@link UnifyWebSessionAttributeConstants#MESSAGEBOX}.
	 * 
	 * @param message    the message to display
	 * @param actionPath the action path
	 * @return response to show application message box
	 * @throws UnifyException if an error occurs
	 */
	protected String showMessageBox(String message, String actionPath) throws UnifyException {
		return showMessageBox(MessageIcon.INFO, MessageMode.OK, "$m{messagebox.message}", message, actionPath);
	}

	/**
	 * Sets up a response that shows a message box with default info icon and OK
	 * button. {@link MessageBox} value of the session attribute
	 * {@link UnifyWebSessionAttributeConstants#MESSAGEBOX}.
	 * 
	 * @param caption    the message caption
	 * @param message    the message to display
	 * @param actionPath the action path
	 * @return response to show application message box
	 * @throws UnifyException if an error occurs
	 */
	protected String showMessageBox(String caption, String message, String actionPath) throws UnifyException {
		return showMessageBox(MessageIcon.INFO, MessageMode.OK, caption, message, actionPath);
	}

	/**
	 * Sets up a response that shows a message box. The message box is backed by the
	 * {@link MessageBox} value of the session attribute
	 * {@link UnifyWebSessionAttributeConstants#MESSAGEBOX}.
	 * 
	 * @param messageIcon the message icon of enumeration type {@link MessageIcon}
	 * @param messageMode the message mode of enumeration type {@link MessageMode}
	 * @param caption     the message caption
	 * @param message     the message to display
	 * @param actionPath  the action path
	 * @return response to show application message box
	 * @throws UnifyException if an error occurs
	 */
	protected String showMessageBox(MessageIcon messageIcon, MessageMode messageMode, String caption, String message,
			String actionPath) throws UnifyException {
		caption = resolveSessionMessage(caption);
		return showMessageBox(messageIcon, messageMode, new MessageBoxCaptions(caption), message, actionPath);
	}

	/**
	 * Sets up a response that shows a message box. The message box is backed by the
	 * {@link MessageBox} value of the session attribute
	 * {@link UnifyWebSessionAttributeConstants#MESSAGEBOX}.
	 * 
	 * @param messageIcon the message icon of enumeration type {@link MessageIcon}
	 * @param messageMode the message mode of enumeration type {@link MessageMode}
	 * @param caption     the message caption
	 * @param message     the message to display
	 * @param actionPath  the action path
	 * @return response to show application message box
	 * @throws UnifyException if an error occurs
	 */
	protected String showMessageBox(MessageIcon messageIcon, MessageMode messageMode, MessageBoxCaptions captions,
			String message, String actionPath) throws UnifyException {
		if (StringUtils.isBlank(actionPath)) {
			actionPath = "/hidePopup";
		}

		message = resolveSessionMessage(message);
		setSessionAttribute(UnifyWebSessionAttributeConstants.MESSAGEBOX,
				new MessageBox(messageIcon, messageMode, captions, message, getName() + actionPath));
		return "showapplicationmessage";
	}

	/**
	 * Launches a task and shows a monitoring box.
	 * 
	 * @param taskSetup the task setup
	 * @param caption   the task monitor box caption
	 * @return the show application monitor box result mapping name
	 * @throws UnifyException if an error occurs
	 */
	protected String launchTaskWithMonitorBox(TaskSetup taskSetup, String caption) throws UnifyException {
		return launchTaskWithMonitorBox(taskSetup, caption, null, null);
	}

	/**
	 * Launches a task and shows a monitoring box.
	 * 
	 * @param taskSetup     the task setup
	 * @param caption       the task monitor box caption
	 * @param onSuccessPath optional on task success forward to path
	 * @param onFailurePath optional on task failure forward to path
	 * @return the show application monitor box result mapping name
	 * @throws UnifyException if an error occurs
	 */
	protected String launchTaskWithMonitorBox(TaskSetup taskSetup, String caption, String onSuccessPath,
			String onFailurePath) throws UnifyException {
		TaskMonitor taskMonitor = launchTask(taskSetup);
		TaskMonitorInfo taskMonitorInfo = new TaskMonitorInfo(taskMonitor, resolveSessionMessage(caption),
				onSuccessPath, onFailurePath);
		setSessionAttribute(UnifyWebSessionAttributeConstants.TASKMONITORINFO, taskMonitorInfo);
		return "showapplicationtaskmonitor";
	}

	/**
	 * Fires action of a page controller in current session.
	 * 
	 * @param fullActionPath the target full action path name
	 * @return the action result mapping
	 * @throws UnifyException if an error occurs
	 */
	protected String fireOtherControllerAction(String fullActionPath) throws UnifyException {
		return getUIControllerUtil().executePageController(fullActionPath);
	}

	/**
	 * Writes value to another page controller's property in current session.
	 * 
	 * @param controllerName the target controller name
	 * @param propertyName   the controller property to set
	 * @param value          the value to set
	 * @throws UnifyException if an error occurs
	 */
	protected void writeOtherControllerProperty(String controllerName, String propertyName, Object value)
			throws UnifyException {
		getUIControllerUtil().populatePageBean(controllerName, propertyName, value);
	}

	/**
	 * Sets the value of attribute in the document context associated with this
	 * controller.
	 * 
	 * @param name  the attribute name
	 * @param value the value to set
	 * @throws UnifyException if an error occurs
	 */
	protected void setDocumentAttribute(String name, Object value) throws UnifyException {
		Document document = getPageRequestContextUtil().getRequestDocument();
		if (document != null) {
			document.setAttribute(name, value);
		}
	}

	/**
	 * Clears an attribute from the document context associated with this
	 * controller.
	 * 
	 * @param name the attribute name
	 * @throws UnifyException if an error occurs
	 */
	protected Object clearDocumentAttribute(String name) throws UnifyException {
		Document document = getPageRequestContextUtil().getRequestDocument();
		if (document != null) {
			return document.clearAttribute(name);
		}
		return null;
	}

	/**
	 * Gets the value of attribute from the document context associated with this
	 * controller.
	 * 
	 * @param name the attribute name
	 * @return the attribute value if found, otherwise null.
	 * @throws UnifyException if an error occurs
	 */
	protected Object getDocumentAttribute(String name) throws UnifyException {
		Document document = getPageRequestContextUtil().getRequestDocument();
		if (document != null) {
			return document.getAttribute(name);
		}
		return null;
	}

	/**
	 * Gets the value of attribute, casted to specified type, from the document
	 * context associated with this controller.
	 * 
	 * @param clazz the type to cast attribute value to
	 * @param name  the attribute name
	 * @return the attribute value if found, otherwise null.
	 * @throws UnifyException if an error occurs
	 */
	@SuppressWarnings("unchecked")
	protected <U> U getDocumentAttribute(Class<U> clazz, String name) throws UnifyException {
		Document document = getPageRequestContextUtil().getRequestDocument();
		if (document != null) {
			return (U) document.getAttribute(name);
		}
		return null;
	}

	/**
	 * Sets the value of attribute in current request page.
	 * 
	 * @param name  the attribute name
	 * @param value the value to set
	 */
	protected void setPageAttribute(String name, Object value) throws UnifyException {
		getPageRequestContextUtil().getRequestPage().setAttribute(name, value);
	}

	/**
	 * Clears an attribute from the current request page.
	 * 
	 * @param name the attribute name
	 * @throws UnifyException if an error occurs
	 */
	protected Object clearPageAttribute(String name) throws UnifyException {
		return getPageRequestContextUtil().getRequestPage().clearAttribute(name);
	}

	/**
	 * Gets the value of attribute from the current request page.
	 * 
	 * @param name the attribute name
	 * @return the attribute value if found, otherwise null.
	 * @throws UnifyException if an error occurs
	 */
	protected Object getPageAttribute(String name) throws UnifyException {
		return getPageRequestContextUtil().getRequestPage().getAttribute(name);
	}

	/**
	 * Gets the value of attribute, casted to specified type, from the current
	 * request page.
	 * 
	 * @param clazz the type to cast attribute value to
	 * @param name  the attribute name
	 * @return the attribute value if found, otherwise null.
	 * @throws UnifyException if an error occurs
	 */
	@SuppressWarnings("unchecked")
	protected <U> U getPageAttribute(Class<U> clazz, String name) throws UnifyException {
		return (U) getPageRequestContextUtil().getRequestPage().getAttribute(name);
	}

	/**
	 * Returns true if page validation is enabled.
	 * 
	 * @throws UnifyException if an error occurs
	 */
	protected boolean isPageValidationEnabled() throws UnifyException {
		return getPageRequestContextUtil().getRequestPage().isValidationEnabled();
	}

	/**
	 * Sets page title.
	 * 
	 * @param title the title to set
	 * @throws UnifyException if an error occurs
	 */
	protected void setPageTitle(String title) throws UnifyException {
		setPageAttribute(PageAttributeConstants.PAGE_TITLE, title);
	}

	/**
	 * Sets the page validation enabled flag.
	 * 
	 * @param validationEnabled the flag to set
	 * @throws UnifyException if an error occurs
	 */
	protected void setPageValidationEnabled(boolean validationEnabled) throws UnifyException {
		getPageRequestContextUtil().getRequestPage().setValidationEnabled(validationEnabled);
	}

	protected <U> U getPageWidgetByLongName(Class<U> clazz, String longName) throws UnifyException {
		return getPageRequestContextUtil().getRequestPage().getWidgetByLongName(clazz, longName);
	}

	protected <U> U getPageWidgetByShortName(Class<U> clazz, String shortName) throws UnifyException {
		return getPageRequestContextUtil().getRequestPage().getWidgetByShortName(clazz, shortName);
	}

	protected void setPageWidgetDisabled(String shortName, boolean disabled) throws UnifyException {
		getPageRequestContextUtil().getRequestPage().setWidgetDisabled(shortName, disabled);
	}

	protected boolean isPageWidgetDisabled(String shortName) throws UnifyException {
		return getPageRequestContextUtil().getRequestPage().isWidgetDisabled(shortName);
	}

	protected void setPageWidgetVisible(String shortName, boolean visible) throws UnifyException {
		getPageRequestContextUtil().getRequestPage().setWidgetVisible(shortName, visible);
	}

	protected boolean isPageWidgetVisible(String shortName) throws UnifyException {
		return getPageRequestContextUtil().getRequestPage().isWidgetVisible(shortName);
	}

	protected void setPageWidgetEditable(String shortName, boolean editable) throws UnifyException {
		getPageRequestContextUtil().getRequestPage().setWidgetEditable(shortName, editable);
	}

	protected boolean isPageWidgetEditable(String shortName) throws UnifyException {
		return getPageRequestContextUtil().getRequestPage().isWidgetEditable(shortName);
	}

	protected void setPageWidgetFocus(String shortName) throws UnifyException {
		getPageRequestContextUtil().getRequestPage().setWidgetFocus(shortName);
	}

	/**
	 * Launches a task using supplied setup.
	 * 
	 * @param taskSetup the task setup
	 * @return the task monitor
	 * @throws UnifyException if an error occurs
	 */
	protected TaskMonitor launchTask(TaskSetup taskSetup) throws UnifyException {
		return taskLauncher.launchTask(taskSetup);
	}

	/**
	 * Hints user in current request with supplied message in INFO mode.
	 * 
	 * @param messageKey the message key
	 * @param params     the message parameters
	 * @throws UnifyException if an error occurs
	 */
	protected void hintUser(String messageKey, Object... params) throws UnifyException {
		getPageRequestContextUtil().hintUser(MODE.INFO, messageKey, params);
	}

	/**
	 * Hints user in current request with supplied message.
	 * 
	 * @param mode       the hint mode
	 * @param messageKey the message key
	 * @param params     the message parameters
	 * @throws UnifyException if an error occurs
	 */
	protected void hintUser(MODE mode, String messageKey, Object... params) throws UnifyException {
		getPageRequestContextUtil().hintUser(mode, messageKey, params);
	}

	/**
	 * Clears hint user in current request.
	 * 
	 * @throws UnifyException if an error occurs
	 */
	protected void clearHintUser() throws UnifyException {
		getPageRequestContextUtil().clearHintUser();
	}

	/**
	 * Returns the current request target object
	 * 
	 * @param clazz the target type
	 * @throws UnifyException if an error occurs
	 */
	protected <U> U getRequestTarget(Class<U> clazz) throws UnifyException {
		return getPageRequestContextUtil().getRequestTargetValue(clazz);
	}

	/**
	 * Sets current client (browser) to listen to topic.
	 * 
	 * @param topic the topic to set
	 * @throws UnifyException if an error occurs
	 */
	protected void setClientListenToTopic(String topic) throws UnifyException {
		getPageRequestContextUtil().setClientTopic(topic);
	}

	/**
	 * Sets current client (browser) to listen to topic with associated title.
	 * 
	 * @param topic the topic to set
	 * @param title the associated title
	 * @throws UnifyException if an error occurs
	 */
	protected void setClientListenToTopic(String topic, String title) throws UnifyException {
		getPageRequestContextUtil().setClientTopic(topic + ":" + title);
	}

	/**
	 * Sets current client (browser) to listen to entity.
	 * 
	 * @param entityClass the entity class
	 * @throws UnifyException if an error occurs
	 */
	protected void setClientListenToEntity(Class<? extends Entity> entityClass) throws UnifyException {
		setClientListenToEntity(entityClass, null);
	}

	/**
	 * Sets current client (browser) to listen to entity with associated entity Id.
	 * 
	 * @param entityClass the entity class
	 * @param id          the entity ID
	 * @throws UnifyException if an error occurs
	 */
	protected void setClientListenToEntity(Class<? extends Entity> entityClass, Object id) throws UnifyException {
		getPageRequestContextUtil()
				.setClientTopic(id != null ? entityClass.getName() + ":" + id : entityClass.getName());
	}

	/**
	 * Adds client topic to current request attribute.
	 * 
	 * @param eventType the event type
	 * @param topic     the topic to set
	 * @throws UnifyException if an error occurs
	 */
	protected void addClientTopicEvent(TopicEventType eventType, String topic) throws UnifyException {
		getPageRequestContextUtil().addClientTopicEvent(eventType, topic);
	}

	/**
	 * Adds client topic to current request attribute.
	 * 
	 * @param eventType the event type
	 * @param topic     the topic to set
	 * @param title     the associated title
	 * @throws UnifyException if an error occurs
	 */
	protected void addClientTopicEvent(TopicEventType eventType, String topic, String title) throws UnifyException {
		getPageRequestContextUtil().addClientTopicEvent(eventType, topic + ":" + title);
	}

	/**
	 * Executes on {@link #initPage()}
	 * 
	 * @throws UnifyException if an error occurs
	 */
	protected void onInitPage() throws UnifyException {

	}

	/**
	 * Executes on {@link #reloadPage()}
	 * 
	 * @throws UnifyException if an error occurs
	 */
	protected void onReloadPage() throws UnifyException {

	}

	/**
	 * Executes on {@link #indexPage()}
	 * 
	 * @throws UnifyException if an error occurs
	 */
	protected void onIndexPage() throws UnifyException {

	}

	/**
	 * Executes on {@link #openPage()}
	 * 
	 * @throws UnifyException if an error occurs
	 */
	protected void onOpenPage() throws UnifyException {

	}

	/**
	 * Executes on {@link #savePage()}
	 * 
	 * @throws UnifyException if an error occurs
	 */
	protected void onSavePage() throws UnifyException {

	}

	/**
	 * Executes on {@link #closePage()}
	 * 
	 * @throws UnifyException if an error occurs
	 */
	protected void onClosePage() throws UnifyException {

	}

	/**
	 * Change page path information.
	 * 
	 * @param colorScheme  the color scheme to set
	 * @param savePagePath the save page path to set
	 * @param remoteSave   the remote save flag
	 * @throws UnifyException if an error occurs
	 */
	protected void changePathInfo(String colorScheme, String savePagePath, boolean remoteSave) throws UnifyException {
		PagePathInfo pathInfo = pagePathInfoRepository.getPagePathInfo(getPage());
		pathInfo.setColorScheme(colorScheme);
		pathInfo.setSavePagePath(savePagePath);
		pathInfo.setRemoteSave(remoteSave);
	}

	protected void closeAllPages() throws UnifyException {
		performClosePage(ClosePageMode.CLOSE_ALL, true);
	}

	protected MessageResult getMessageResult() throws UnifyException {
		return getRequestTarget(MessageResult.class);
	}

	protected void setResultMapping(String resultMappingName) throws UnifyException {
		getPageRequestContextUtil().setCommandResultMapping(resultMappingName);
	}

	private Page resolveRequestPage() throws UnifyException {
		PageRequestContextUtil rcUtil = getPageRequestContextUtil();
		Page contentPage = rcUtil.getContentPage();
		return contentPage == null ? rcUtil.getRequestPage() : contentPage;
	}

	private void performClosePage(ClosePageMode closePageMode, boolean isFireClose) throws UnifyException {
		PageRequestContextUtil pageRequestContextUtil = getPageRequestContextUtil();
		Page currentPage = pageRequestContextUtil.getRequestPage();
		ContentPanel contentPanel = pageRequestContextUtil.getRequestDocument().getContentPanel();
		List<String> toClosePathIdList = contentPanel.evaluateRemoveContent(currentPage, closePageMode);
		performClosePages(contentPanel, toClosePathIdList, isFireClose);
	}

	private void performClosePages(ContentPanel contentPanel, List<String> toClosePathIdList, boolean isFireClose)
			throws UnifyException {
		if (!toClosePathIdList.isEmpty()) {
			PageRequestContextUtil pageRequestContextUtil = getPageRequestContextUtil();
			Page currentPage = pageRequestContextUtil.getRequestPage();
			try {
				if (isFireClose) {
					// Fire closePage() for all targets
					for (String closePathId : toClosePathIdList) {
						ControllerPathParts controllerPathParts = getPathInfoRepository()
								.getControllerPathParts(closePathId);
						getUIControllerUtil().loadRequestPage(controllerPathParts);
						((PageController<?>) getComponent(controllerPathParts.getControllerName())).closePage();
					}
				}

				// Do actual content removal
				contentPanel.removeContent(toClosePathIdList);

				// Set pages for removal
				pageRequestContextUtil.setClosedPagePaths(toClosePathIdList);
			} finally {
				// Restore request page
				pageRequestContextUtil.setRequestPage(currentPage);
			}
		}
	}

	private boolean validate(Page page, DataTransfer dataTransfer) throws UnifyException {
		boolean successful = true;
		if (page.isValidationEnabled()) {
			String actionId = dataTransfer.getActionId();
			logDebug("Page validation is enabled. actionId = [{0}]", actionId);

			if (StringUtils.isNotBlank(actionId)) {
				logDebug("Performing request parameter validation. page ID [{0}]", page.getPageId());

				// Do validations
				PageAction pageAction = page.getPageAction(getPageManager().getLongName(actionId));
				UplElementReferences uer = pageAction.getUplAttribute(UplElementReferences.class, "validations");
				for (String validationLongName : uer.getLongNames()) {
					logDebug("Applying validation [{0}]...", validationLongName);
					successful &= page.getPageWidgetValidator(getPageManager(), validationLongName)
							.validate(dataTransfer);
				}

				logDebug("Request parameter validation completed. page ID [{0}]", page.getPageId());
			}
		}

		return successful;
	}
}
