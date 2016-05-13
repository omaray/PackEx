package com.packex;

public class Constants {
    public static final String PACKAGES_FILE_PATH = "./resources/packages.json";

    public static final String RUBY_URL_TEMPLATE = "https://rubygems.org/api/v1/gems/%s.json";
    public static final String PHP_URL_TEMPLATE = "https://packagist.org/packages/%s.json";
    
    // Package management URL sites
    public static final String NODE_DAY_URL_TEMPLATE = "https://api.npmjs.org/downloads/point/last-day/%s";
    public static final String NODE_WEEK_URL_TEMPLATE = "https://api.npmjs.org/downloads/point/last-week/%s";
    public static final String NODE_MONTH_URL_TEMPLATE = "https://api.npmjs.org/downloads/point/last-month/%s";
    
    // BigQuery table fields
    public static final String DATE_FIELD = "date";
    public static final String LANGUAGE_FIELD = "language";
    public static final String PACKAGE_NAME_FIELD = "package_name";
    public static final String VERSION_FIELD = "version";
    public static final String CATEGORY_FIELD = "category";
    public static final String TOTAL_DOWNLOADS_FIELD = "total_downloads";
    public static final String MONTHLY_DOWNLOADS_FIELD = "monthly_downloads";
    public static final String WEEKLY_DOWNLOADS_FIELD = "weekly_downloads";
    public static final String DAILY_DOWNLOADS_FIELD = "daily_downloads";
    public static final String MONTHLY_CALCULATED_DOWNLOADS_FIELD = "monthly_calculated_downloads";
    public static final String WEEKLY_CALCULATED_DOWNLOADS_FIELD = "weekly_calculated_downloads";
    public static final String DAILY_CALCULATED_DOWNLOADS_FIELD = "daily_calculated_downloads";
}
