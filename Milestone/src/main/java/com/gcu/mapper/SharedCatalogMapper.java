package com.gcu.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gcu.model.SharedCatalogModel;

public class SharedCatalogMapper implements RowMapper<SharedCatalogModel> {

    @Override
    public SharedCatalogModel mapRow(ResultSet rs, int rowNum) throws SQLException {
        SharedCatalogModel m = new SharedCatalogModel();
        m.setId(rs.getInt("id"));
        m.setCatalogId(rs.getInt("catalog_id"));
        m.setSharedUserId(rs.getInt("shared_user_id"));
        m.setPermission(rs.getString("permission"));
        m.setCreatedDate(rs.getTimestamp("created_date"));
        m.setSharedUsername(rs.getString("username"));
        return m;
    }
}