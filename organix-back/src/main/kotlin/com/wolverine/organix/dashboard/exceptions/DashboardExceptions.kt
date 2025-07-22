package com.wolverine.organix.dashboard.exceptions

class DashboardNotFoundException(message: String) : RuntimeException(message)

class DashboardWidgetNotFoundException(message: String) : RuntimeException(message)

class DashboardValidationException(message: String) : RuntimeException(message)

class DashboardLayoutLimitExceededException(message: String) : RuntimeException(message)

class DashboardWidgetLimitExceededException(message: String) : RuntimeException(message)

class UserPreferencesNotFoundException(message: String) : RuntimeException(message)

class DashboardUnauthorizedException(message: String) : RuntimeException(message)

class WidgetNotFoundException(message: String) : RuntimeException(message)
