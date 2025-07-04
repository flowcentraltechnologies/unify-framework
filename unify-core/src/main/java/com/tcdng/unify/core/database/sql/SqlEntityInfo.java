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
package com.tcdng.unify.core.database.sql;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tcdng.unify.common.annotation.EntityConnect;
import com.tcdng.unify.common.constants.ConnectEntityBaseType;
import com.tcdng.unify.common.constants.ConnectFieldDataType;
import com.tcdng.unify.common.constants.EnumConst;
import com.tcdng.unify.common.data.EntityDTO;
import com.tcdng.unify.common.data.EntityInfo;
import com.tcdng.unify.common.database.Entity;
import com.tcdng.unify.core.UnifyCoreErrorConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.database.EntityPolicy;
import com.tcdng.unify.core.database.MappedEntityRepository;
import com.tcdng.unify.core.util.AnnotationUtils;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.NameUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Holds entity information.
 * 
 * @author The Code Department
 * @since 4.1
 */
public class SqlEntityInfo implements SqlEntitySchemaInfo {

	private Long index;

	private Class<? extends Entity> entityClass;

	private Class<? extends EnumConst> enumConstClass;

	private EntityPolicy entityPolicy;

	private MappedEntityRepository mappedEntityRepository;

	private String schema;

	private String tableName;

	private String preferredTableName;

	private String schemaTableName;

	private String tableAlias;

	private String viewName;

	private String preferredViewName;

	private String schemaViewName;

	private SqlEntityInfo extensionSqlEntityInfo;

	private SqlFieldInfo idFieldInfo;

	private SqlFieldInfo versionFieldInfo;

	private SqlFieldInfo tenantIdFieldInfo;

	private SqlFieldInfo fosterParentTypeFieldInfo;

	private SqlFieldInfo fosterParentIdFieldInfo;

	private SqlFieldInfo categoryFieldInfo;

	private List<SqlFieldInfo> fieldInfoList;

	private List<SqlFieldInfo> listFieldInfoList;

	private Map<String, SqlFieldInfo> fieldInfoByName;

	private Map<String, SqlFieldInfo> listFieldInfoByName;

	private Map<Long, SqlFieldInfo> listInfoMapByIndex;

	private List<SqlForeignKeyInfo> foreignKeyList;

	private List<SqlFieldInfo> managedFieldInfoList;

	private List<SqlFieldInfo> managedListFieldInfoList;

	private Map<String, SqlFieldInfo> managedFieldInfoByName;

	private List<SqlForeignKeyInfo> managedForeignKeyList;

	private Map<String, ChildFieldInfo> childInfoByName;

	private List<SqlQueryRestrictionInfo> defaultRestrictionList;

	private List<ChildFieldInfo> childInfoList;

	private List<ChildFieldInfo> childListInfoList;

	private List<OnDeleteCascadeInfo> onDeleteCascadeInfoList;

	private Map<String, SqlUniqueConstraintInfo> uniqueConstraintMap;

	private Map<String, SqlIndexInfo> indexMap;

	private List<Map<String, Object>> staticValueList;

	private Map<String, Class<?>> viewBaseTables;

	private List<SqlViewRestrictionInfo> viewRestrictionList;

	private List<Set<String>> uniqueConstraints;

	private boolean identityManaged;

	private boolean schemaAlreadyManaged;

	private EntityDTO entityDTO;
	
	public SqlEntityInfo(Long index, Class<? extends Entity> entityClass, Class<? extends EnumConst> enumConstClass,
			EntityPolicy recordPolicy, MappedEntityRepository mappedEntityRepository, String schema,
			String tableName, String preferredTableName, String schemaTableName, String tableAlias, String viewName,
			String preferredViewName, String schemaViewName, SqlFieldInfo idFieldInfo, SqlFieldInfo versionFieldInfo,
			SqlFieldInfo tenantIdFieldInfo, SqlFieldInfo fosterParentTypeFieldInfo,
			SqlFieldInfo fosterParentIdFieldInfo, SqlFieldInfo categoryFieldInfo,
			Map<String, SqlFieldInfo> sQLFieldInfoMap, List<SqlQueryRestrictionInfo> defaultRestrictionList,
			List<ChildFieldInfo> childInfoList, List<ChildFieldInfo> childListInfoList,
			Map<String, SqlUniqueConstraintInfo> uniqueConstraintMap, Map<String, SqlIndexInfo> indexMap,
			List<Map<String, Object>> staticValueList, Map<String, Class<?>> viewBaseTables,
			List<SqlViewRestrictionInfo> viewRestrictionList, boolean isAllObjectsInLowerCase, boolean identityManaged)
			throws UnifyException {
		this.index = index;
		this.entityClass = entityClass;
		this.enumConstClass = enumConstClass;
		this.entityPolicy = recordPolicy;
		this.mappedEntityRepository = mappedEntityRepository;
		this.schema = schema;
		this.tableName = tableName;
		this.preferredTableName = preferredTableName;
		this.schemaTableName = schemaTableName;
		this.tableAlias = tableAlias;
		this.viewName = viewName;
		this.preferredViewName = preferredViewName;
		this.schemaViewName = schemaViewName;
		this.idFieldInfo = idFieldInfo;
		this.versionFieldInfo = versionFieldInfo;
		this.tenantIdFieldInfo = tenantIdFieldInfo;
		this.fosterParentTypeFieldInfo = fosterParentTypeFieldInfo;
		this.fosterParentIdFieldInfo = fosterParentIdFieldInfo;
		this.categoryFieldInfo = categoryFieldInfo;
		this.schemaAlreadyManaged = false;

		if (isAllObjectsInLowerCase) {
			this.tableName = StringUtils.toLowerCase(tableName);
			this.preferredTableName = StringUtils.toLowerCase(preferredTableName);
			this.schemaTableName = StringUtils.toLowerCase(schemaTableName);
			this.tableAlias = StringUtils.toLowerCase(tableAlias);
			this.viewName = StringUtils.toLowerCase(viewName);
			this.preferredViewName = StringUtils.toLowerCase(preferredViewName);
			this.schemaViewName = StringUtils.toLowerCase(schemaViewName);
		}

		initFieldInfos(sQLFieldInfoMap);

		this.defaultRestrictionList = DataUtils.unmodifiableList(defaultRestrictionList);
		this.childInfoList = DataUtils.unmodifiableList(childInfoList);
		this.childListInfoList = DataUtils.unmodifiableList(childListInfoList);
		this.childInfoByName = null;

		if (this.childInfoList.isEmpty() && this.childListInfoList.isEmpty()) {
			this.onDeleteCascadeInfoList = Collections.emptyList();
		} else {
			this.onDeleteCascadeInfoList = new ArrayList<OnDeleteCascadeInfo>(this.childInfoList);
			this.onDeleteCascadeInfoList.addAll(this.childListInfoList);
			this.onDeleteCascadeInfoList = Collections.unmodifiableList(this.onDeleteCascadeInfoList);

			this.childInfoByName = new HashMap<String, ChildFieldInfo>();
			for (OnDeleteCascadeInfo info : this.onDeleteCascadeInfoList) {
				ChildFieldInfo childFieldInfo = (ChildFieldInfo) info;
				this.childInfoByName.put(childFieldInfo.getName(), childFieldInfo);
			}
		}

		this.childInfoByName = DataUtils.unmodifiableMap(this.childInfoByName);
		this.staticValueList = staticValueList;
		this.viewBaseTables = DataUtils.unmodifiableMap(viewBaseTables);
		this.viewRestrictionList = DataUtils.unmodifiableList(viewRestrictionList);
		this.uniqueConstraintMap = DataUtils.unmodifiableMap(uniqueConstraintMap);
		this.indexMap = DataUtils.unmodifiableMap(indexMap);
		this.identityManaged = identityManaged;
	}

	private SqlEntityInfo(SqlEntityInfo originSqlEntityInfo, Class<? extends Entity> entityClass,
			Map<String, SqlFieldInfo> sQLFieldInfoMap, boolean identityManaged) throws UnifyException {
		this.index = originSqlEntityInfo.index;
		this.entityClass = entityClass;
		this.enumConstClass = originSqlEntityInfo.enumConstClass;
		this.entityPolicy = originSqlEntityInfo.entityPolicy;
		this.schema = originSqlEntityInfo.schema;
		this.tableName = originSqlEntityInfo.tableName;
		this.preferredTableName = originSqlEntityInfo.preferredTableName;
		this.schemaTableName = originSqlEntityInfo.schemaTableName;
		this.tableAlias = originSqlEntityInfo.tableAlias;
		this.viewName = originSqlEntityInfo.viewName;
		this.preferredViewName = originSqlEntityInfo.preferredViewName;
		this.schemaViewName = originSqlEntityInfo.schemaViewName;
		this.idFieldInfo = originSqlEntityInfo.idFieldInfo;
		this.versionFieldInfo = originSqlEntityInfo.versionFieldInfo;
		this.tenantIdFieldInfo = originSqlEntityInfo.tenantIdFieldInfo;
		this.fosterParentTypeFieldInfo = originSqlEntityInfo.fosterParentTypeFieldInfo;
		this.fosterParentIdFieldInfo = originSqlEntityInfo.fosterParentIdFieldInfo;
		this.categoryFieldInfo = originSqlEntityInfo.categoryFieldInfo;
		this.defaultRestrictionList = originSqlEntityInfo.defaultRestrictionList;
		this.childInfoList = originSqlEntityInfo.childInfoList;
		this.childListInfoList = originSqlEntityInfo.childListInfoList;
		this.childInfoByName = originSqlEntityInfo.childInfoByName;
		this.onDeleteCascadeInfoList = originSqlEntityInfo.onDeleteCascadeInfoList;
		this.staticValueList = originSqlEntityInfo.staticValueList;
		this.viewBaseTables = originSqlEntityInfo.viewBaseTables;
		this.viewRestrictionList = originSqlEntityInfo.viewRestrictionList;
		this.uniqueConstraintMap = originSqlEntityInfo.uniqueConstraintMap;
		this.indexMap = originSqlEntityInfo.indexMap;
		this.mappedEntityRepository = originSqlEntityInfo.mappedEntityRepository;
		this.schemaAlreadyManaged = false;
		this.identityManaged = identityManaged;

		if (sQLFieldInfoMap != null) {
			Map<String, SqlFieldInfo> newSQLFieldInfoMap = new LinkedHashMap<String, SqlFieldInfo>(
					originSqlEntityInfo.listFieldInfoByName);
			newSQLFieldInfoMap.putAll(sQLFieldInfoMap);
			initFieldInfos(newSQLFieldInfoMap);
		} else {
			initFieldInfos(originSqlEntityInfo.listFieldInfoByName);
		}
	}

	@Override
	public EntityDTO getEntityDTO() throws Exception {
		if (entityDTO == null) {
			synchronized (this) {
				if (entityDTO == null) {
					EntityConnect eca = entityClass.getAnnotation(EntityConnect.class);
					if (eca != null) {
						EntityInfo.Builder eib = EntityInfo.newBuilder();
						final String id = AnnotationUtils.getAnnotationString(eca.id());
						final String versionNo = AnnotationUtils.getAnnotationString(eca.versionNo());
						final String description = AnnotationUtils.getAnnotationString(eca.description());
						eib.baseType(eca.base()).dataSourceAlias(AnnotationUtils.getAnnotationString(eca.datasource()))
								.name(ensureLongName(eca.application(), eca.entity())).tableName(tableName)
								.description(description != null ? description : NameUtils.describeName(eca.entity()))
								.implementation(entityClass.getName()).idFieldName(id != null ? id : "id")
								.versionNoFieldName(versionNo != null ? versionNo : "versionNo")
								.actionPolicy(eca.actionPolicy());
						populateBaseFields(eib, eca.base());
						for (SqlFieldInfo sqlFieldInfo : fieldInfoList) {
							if (!eib.isPresent(sqlFieldInfo.getName())) {
								eib.addField(sqlFieldInfo.getColumnType().connectType(),
										sqlFieldInfo.getFieldType().isEnum() ? String.class
												: sqlFieldInfo.getFieldType(),
										sqlFieldInfo.getName(), NameUtils.describeName(sqlFieldInfo.getName()),
										sqlFieldInfo.getColumnName(),
										sqlFieldInfo.isForeignKey()
												&& sqlFieldInfo.getForeignEntityInfo().isWithEntityDTO()
														? sqlFieldInfo.getForeignEntityInfo().getEntityDTO().getName()
														: null,
										sqlFieldInfo.getFieldType().isEnum() ? sqlFieldInfo.getFieldType().getName()
												: null,
										sqlFieldInfo.getPrecision(), sqlFieldInfo.getScale(), sqlFieldInfo.getLength(),
										sqlFieldInfo.isNullable());
							}
						}

						entityDTO = new EntityDTO(eib.build());
					}
				}
			}
		}

		return entityDTO;
	}

	@Override
	public boolean isWithEntityDTO() {
		return entityClass.isAnnotationPresent(EntityConnect.class);
	}

	@Override
	public Long getIndex() {
		return index;
	}

	@Override
	public String getSchema() {
		return schema;
	}

	@Override
	public String getTableName() {
		return tableName;
	}

	@Override
	public String getPreferredTableName() {
		return preferredTableName;
	}

	@Override
	public String getSchemaTableName() {
		return schemaTableName;
	}

	@Override
	public String getTableAlias() {
		return tableAlias;
	}

	@Override
	public String getViewName() {
		return viewName;
	}

	@Override
	public String getPreferredViewName() {
		return preferredViewName;
	}

	@Override
	public String getSchemaViewName() {
		return schemaViewName;
	}

	@Override
	public SqlFieldInfo getIdFieldInfo() {
		return idFieldInfo;
	}

	@Override
	public SqlFieldInfo getVersionFieldInfo() {
		return versionFieldInfo;
	}

	@Override
	public SqlFieldInfo getFosterParentTypeFieldInfo() {
		return fosterParentTypeFieldInfo;
	}

	@Override
	public SqlFieldInfo getFosterParentIdFieldInfo() {
		return fosterParentIdFieldInfo;
	}

	@Override
	public SqlFieldInfo getCategoryFieldInfo() {
		return categoryFieldInfo;
	}

	@Override
	public Set<String> getFieldNames() {
		return fieldInfoByName.keySet();
	}

	@Override
	public List<SqlFieldInfo> getFieldInfos() {
		return fieldInfoList;
	}

	@Override
	public List<SqlFieldInfo> getManagedFieldInfos() {
		return managedFieldInfoList;
	}

	public List<SqlForeignKeyInfo> getManagedForeignKeyList() {
		return managedForeignKeyList;
	}

	@Override
	public List<SqlFieldInfo> getListFieldInfos() {
		return listFieldInfoList;
	}

	@Override
	public List<SqlFieldInfo> getManagedListFieldInfos() {
		return managedListFieldInfoList;
	}

	@Override
	public SqlFieldInfo getTenantIdFieldInfo() {
		return tenantIdFieldInfo;
	}

	@Override
	public boolean isWithTenantId() {
		return tenantIdFieldInfo != null;
	}

	@Override
	public SqlFieldInfo getFieldInfo(String name) throws UnifyException {
		SqlFieldInfo sqlFieldInfo = fieldInfoByName.get(name);
		if (sqlFieldInfo == null) {
			throw new UnifyException(UnifyCoreErrorConstants.RECORD_FIELDINFO_NOT_FOUND, entityClass, name);
		}
		return sqlFieldInfo;
	}

	@Override
	public SqlFieldInfo getManagedFieldInfo(String name) throws UnifyException {
		SqlFieldInfo sqlFieldInfo = managedFieldInfoByName.get(name);
		if (sqlFieldInfo == null) {
			throw new UnifyException(UnifyCoreErrorConstants.RECORD_FIELDINFO_NOT_FOUND, entityClass, name);
		}
		return sqlFieldInfo;
	}

	@Override
	public SqlFieldSchemaInfo getFieldInfo(Long index) throws UnifyException {
		return listInfoMapByIndex.get(index);
	}

	@Override
	public boolean isIdentityManaged() {
		return identityManaged;
	}

	@Override
	public boolean isMapped() {
		return mappedEntityRepository != null;
	}

	@Override
	public MappedEntityRepository getMappedEntityRepository() {
		return mappedEntityRepository;
	}

	@Override
	public boolean isForeignKeys() {
		return !foreignKeyList.isEmpty();
	}

	public boolean isManagedForeignKeys() {
		return !managedForeignKeyList.isEmpty();
	}

	@Override
	public boolean isUniqueConstraints() {
		return uniqueConstraintMap != null && !uniqueConstraintMap.isEmpty();
	}

	@Override
	public Map<String, SqlUniqueConstraintInfo> getUniqueConstraintList() {
		return uniqueConstraintMap;
	}

	@Override
	public boolean isIndexes() {
		return indexMap != null && !indexMap.isEmpty();
	}

	@Override
	public Map<String, SqlIndexInfo> getIndexList() {
		return indexMap;
	}

	@Override
	public List<Map<String, Object>> getStaticValueList() {
		return staticValueList;
	}

	@Override
	public boolean isViewOnly() {
		return !viewBaseTables.isEmpty();
	}

	@Override
	public Map<String, Class<?>> getViewBaseTables() {
		return viewBaseTables;
	}

	@Override
	public boolean isViewRestriction() {
		return !viewRestrictionList.isEmpty();
	}

	@Override
	public List<SqlViewRestrictionInfo> getViewRestrictionList() {
		return viewRestrictionList;
	}

	@Override
	public List<SqlForeignKeyInfo> getForeignKeyList() {
		return foreignKeyList;
	}

	public List<Set<String>> getUniqueConstraints() {
		if (uniqueConstraints == null) {
			synchronized (this) {
				if (uniqueConstraints == null) {
					if (!DataUtils.isBlank(uniqueConstraintMap)) {
						uniqueConstraints = new ArrayList<Set<String>>();
						for (SqlUniqueConstraintInfo info : uniqueConstraintMap.values()) {
							uniqueConstraints.add(new HashSet<String>(info.getFieldNameList()));
						}

						uniqueConstraints = Collections.unmodifiableList(uniqueConstraints);
					} else {
						uniqueConstraints = Collections.emptyList();
					}
				}
			}
		}

		return uniqueConstraints;
	}

	public boolean isChildSqlEntityInfo(Class<?> clazz) {
		for (SqlForeignKeyInfo sqlForeignKeyInfo : foreignKeyList) {
			if (clazz.equals(sqlForeignKeyInfo.getSqlFieldInfo().getForeignEntityInfo().getEntityClass())) {
				return true;
			}
		}

		return false;
	}

	public boolean isSchemaAlreadyManaged() {
		return schemaAlreadyManaged;
	}

	public void setSchemaAlreadyManaged() {
		this.schemaAlreadyManaged = true;
	}

	public Class<? extends Entity> getEntityClass() {
		return entityClass;
	}

	public Class<? extends EnumConst> getEnumConstClass() {
		return enumConstClass;
	}

	public Class<?> getKeyClass() {
		if (enumConstClass != null) {
			return enumConstClass;
		}
		return entityClass;
	}

	public EntityPolicy getEntityPolicy() {
		return entityPolicy;
	}

	public Set<String> getListFieldNames() {
		return listFieldInfoByName.keySet();
	}

	public Map<String, String> getListColumnsByFieldNames() {
		Map<String, String> map = new HashMap<String, String>();
		for (Map.Entry<String, SqlFieldInfo> entry : listFieldInfoByName.entrySet()) {
			map.put(entry.getKey(), entry.getValue().getPreferredColumnName());
		}
		return map;
	}

	public Map<String, SqlFieldInfo> getFieldInfoByColumnNames() {
		Map<String, SqlFieldInfo> map = new HashMap<String, SqlFieldInfo>();
		for (SqlFieldInfo entry : fieldInfoByName.values()) {
			map.put(entry.getColumnName(), entry);
		}
		return map;
	}

	public ChildFieldInfo getChildFieldInfo(String name) {
		return childInfoByName.get(name);
	}

	public boolean isChildFieldInfo(String name) {
		return childInfoByName.containsKey(name);
	}

	public List<ChildFieldInfo> getSingleChildInfoList() {
		return childInfoList;
	}

	public boolean isSingleChildList() {
		return !childInfoList.isEmpty();
	}

	public boolean isWithDefaultRestrictions() {
		return !defaultRestrictionList.isEmpty();
	}

	public List<SqlQueryRestrictionInfo> getDefaultRestrictionList() {
		return defaultRestrictionList;
	}

	public List<ChildFieldInfo> getManyChildInfoList() {
		return childListInfoList;
	}

	public boolean isManyChildList() {
		return !childListInfoList.isEmpty();
	}

	public boolean isChildList() {
		return !childInfoList.isEmpty() || !childListInfoList.isEmpty();
	}

	public List<OnDeleteCascadeInfo> getOnDeleteCascadeInfoList() {
		return onDeleteCascadeInfoList;
	}

	public boolean isOnDeleteCascadeList() {
		return !onDeleteCascadeInfoList.isEmpty();
	}

	public SqlEntityInfo getExtensionSqlEntityInfo() {
		return extensionSqlEntityInfo;
	}

	public boolean isExtended() {
		return extensionSqlEntityInfo != null;
	}

	public boolean isWithFosterParents() {
		return fosterParentTypeFieldInfo != null;
	}

	public boolean isCategoryColumn() {
		return categoryFieldInfo != null;
	}

	SqlEntityInfo extend(String viewName, String preferredViewName, String schemaViewName,
			Class<? extends Entity> extensionEntityClass, Map<String, SqlFieldInfo> sQLFieldInfoMap,
			Map<String, SqlUniqueConstraintInfo> uniqueConstraintMap, Map<String, SqlIndexInfo> indexMap,
			boolean deprecateExtension) throws UnifyException {
		if (isExtended() && !deprecateExtension) {
			throw new UnifyException(UnifyCoreErrorConstants.RECORD_SUPERCLASS_ALREADY_EXTENDED, extensionEntityClass,
					entityClass, extensionSqlEntityInfo.getEntityClass());
		}

		this.viewName = viewName;
		this.preferredViewName = preferredViewName;
		this.schemaViewName = schemaViewName;

		List<SqlForeignKeyInfo> newManagedForeignKeyList = new ArrayList<SqlForeignKeyInfo>(this.managedForeignKeyList);
		Map<String, SqlFieldInfo> newManagedFieldInfoByName = new LinkedHashMap<String, SqlFieldInfo>(
				this.managedFieldInfoByName);
		List<SqlFieldInfo> newManagedFieldInfoList = new ArrayList<SqlFieldInfo>(this.managedFieldInfoList);
		List<SqlFieldInfo> newManagedListFieldInfoList = new ArrayList<SqlFieldInfo>(this.managedListFieldInfoList);

		for (SqlFieldInfo sqlFieldInfo : sQLFieldInfoMap.values()) {
			if (!sqlFieldInfo.isListOnly()) {
				newManagedFieldInfoByName.put(sqlFieldInfo.getName(), sqlFieldInfo);
				newManagedFieldInfoList.add(sqlFieldInfo);
				if (sqlFieldInfo.isForeignKey()) {
					newManagedForeignKeyList.add(new SqlForeignKeyInfo(sqlFieldInfo));
				}
			}

			newManagedListFieldInfoList.add(sqlFieldInfo);
		}
		this.managedForeignKeyList = Collections.unmodifiableList(newManagedForeignKeyList);
		this.managedFieldInfoByName = Collections.unmodifiableMap(newManagedFieldInfoByName);
		this.managedFieldInfoList = Collections.unmodifiableList(newManagedFieldInfoList);
		this.managedListFieldInfoList = Collections.unmodifiableList(newManagedListFieldInfoList);

		if (uniqueConstraintMap != null) {
			Map<String, SqlUniqueConstraintInfo> newUniqueConstraintMap = new LinkedHashMap<String, SqlUniqueConstraintInfo>(
					this.uniqueConstraintMap);
			newUniqueConstraintMap.putAll(uniqueConstraintMap);
			this.uniqueConstraintMap = DataUtils.unmodifiableMap(newUniqueConstraintMap);
		}

		if (indexMap != null) {
			Map<String, SqlIndexInfo> newIndexMap = new LinkedHashMap<String, SqlIndexInfo>(this.indexMap);
			newIndexMap.putAll(indexMap);
			this.indexMap = DataUtils.unmodifiableMap(indexMap);
		}

		extensionSqlEntityInfo = new SqlEntityInfo(this, (Class<? extends Entity>) extensionEntityClass,
				sQLFieldInfoMap, this.identityManaged);
		return extensionSqlEntityInfo;
	}

	void expandOnDeleteCascade(OnDeleteCascadeInfo onDeleteCascadeInfo) {
		List<OnDeleteCascadeInfo> list = new ArrayList<OnDeleteCascadeInfo>(onDeleteCascadeInfoList);
		list.add(onDeleteCascadeInfo);
		onDeleteCascadeInfoList = Collections.unmodifiableList(list);
	}

	public boolean isField(String name) {
		return fieldInfoByName.containsKey(name);
	}

	public SqlFieldInfo getListFieldInfo(String name) throws UnifyException {
		SqlFieldInfo sqlFieldInfo = listFieldInfoByName.get(name);
		if (sqlFieldInfo == null) {
			throw new UnifyException(UnifyCoreErrorConstants.RECORD_LISTFIELDINFO_NOT_FOUND, entityClass, name);
		}
		return sqlFieldInfo;
	}

	public boolean isListField(String name) {
		return listFieldInfoByName.containsKey(name);
	}

	public boolean isVersioned() {
		return this.versionFieldInfo != null;
	}

	public boolean isViewable() {
		return !tableName.equals(viewName);
	}

	public boolean isEnumConst() {
		return enumConstClass != null;
	}

	public boolean testTrueFieldNamesOnly(Collection<String> fieldNames) {
		return managedFieldInfoByName.keySet().containsAll(fieldNames);
	}

    private void populateBaseFields(EntityInfo.Builder eib, ConnectEntityBaseType base) throws Exception {
        switch (base) {
            case BASE_WORK_ENTITY:
                eib.addField(ConnectFieldDataType.STRING, String.class, "workBranchCode", "Work Branch Code",
                        "work_branch_cd");
                eib.addField(ConnectFieldDataType.BOOLEAN, Boolean.class, "inWorkflow", "In Workflow",
                        "in_workflow_fg");
                eib.addField(ConnectFieldDataType.LONG, Long.class, "originalCopyId", "Original Copy ID",
                        "original_copy_id");
                eib.addField(ConnectFieldDataType.STRING, String.class, "wfItemVersionType", "Work Item Version Type",
                        "wf_item_version_type");
            case BASE_AUDIT_ENTITY:
                eib.addField(ConnectFieldDataType.STRING, String.class, "createdBy", "Created By", "created_by");
                eib.addField(ConnectFieldDataType.STRING, String.class, "updatedBy", "Updated By", "updated_by");
                eib.addField(ConnectFieldDataType.TIMESTAMP, Date.class, "createDt", "Created On", "created_on");
                eib.addField(ConnectFieldDataType.TIMESTAMP, Date.class, "updateDt", "Updated On", "updated_on");
            case BASE_VERSION_ENTITY:
                eib.addField(ConnectFieldDataType.LONG, long.class, "versionNo", "Version No.", "version_no");
            case BASE_ENTITY:
                eib.addField(ConnectFieldDataType.LONG, Long.class, "id", "ID", "id");
            default:
                break;
        }
    }

    private String ensureLongName(String applicationName, String name) {
        if (name != null && !name.trim().isEmpty() && name.indexOf('.') < 0) {
            return applicationName + "." + name;
        }

        return name;
    }

	private void initFieldInfos(Map<String, SqlFieldInfo> sQLFieldInfoMap) throws UnifyException {
		this.fieldInfoList = new ArrayList<SqlFieldInfo>();
		this.listFieldInfoList = new ArrayList<SqlFieldInfo>();
		this.listFieldInfoByName = Collections.unmodifiableMap(sQLFieldInfoMap);
		this.foreignKeyList = new ArrayList<SqlForeignKeyInfo>();
		this.fieldInfoByName = new HashMap<String, SqlFieldInfo>();

		List<SqlFieldInfo> inputlistFieldInfoList = new ArrayList<SqlFieldInfo>(listFieldInfoByName.values());
		DataUtils.sortAscending(inputlistFieldInfoList, SqlFieldSchemaInfo.class, "foreignEntityPreferredAlias");
		for (SqlFieldInfo sqlFieldInfo : inputlistFieldInfoList) {
			if (sqlFieldInfo.isListOnly()) {
				if (sqlFieldInfo.isUnresolvedListOnly()) {
					sqlFieldInfo.resolveListOnly(this);
				}
			} else {
				fieldInfoByName.put(sqlFieldInfo.getName(), sqlFieldInfo);
				fieldInfoList.add(sqlFieldInfo);
				if (sqlFieldInfo.isForeignKey()) {
					this.foreignKeyList.add(new SqlForeignKeyInfo(sqlFieldInfo));
				}

				if (sqlFieldInfo.isUnresolvedForeignKey()) {
					sqlFieldInfo.resolveForeignKey(this, idFieldInfo);
				}
			}

			if (sqlFieldInfo.getMarker() != null) {
				if (this.listInfoMapByIndex == null) {
					this.listInfoMapByIndex = new HashMap<Long, SqlFieldInfo>();
				}
				listInfoMapByIndex.put(sqlFieldInfo.getMarker(), sqlFieldInfo);
			}

			listFieldInfoList.add(sqlFieldInfo);
		}

		this.foreignKeyList = DataUtils.unmodifiableList(this.foreignKeyList);
		this.fieldInfoByName = DataUtils.unmodifiableMap(this.fieldInfoByName);
		this.fieldInfoList = DataUtils.unmodifiableList(this.fieldInfoList);
		this.listFieldInfoList = DataUtils.unmodifiableList(this.listFieldInfoList);
		this.managedForeignKeyList = this.foreignKeyList;
		this.managedFieldInfoByName = this.fieldInfoByName;
		this.managedFieldInfoList = this.fieldInfoList;
		this.managedListFieldInfoList = this.listFieldInfoList;
	}

}
