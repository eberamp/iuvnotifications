package mx.edu.iuv.monitor.infrastructure.output.database.query

object TeacherQuery {

    const val GET_ALL_TEACHERS_COURSE_INACTIVE_24_HOURS = """
        SELECT 
            teacher.id AS userId,
            teacher.firstname,
            teacher.email,
            course.id AS courseId,
            course.shortname AS courseShortname,
            enrolments.status AS courseUserActive,
            teacher.lastaccess AS lastAccess,
            accessLog.timeaccess AS courseLastAccess,
            DATEDIFF(FROM_UNIXTIME(teacher.lastaccess), FROM_UNIXTIME(accessLog.timeaccess)) AS lastAccessInDays
        FROM 
            mo_user AS teacher
        JOIN 
            mo_user_enrolments AS enrolments 
            ON enrolments.userid = teacher.id
        JOIN 
            mo_enrol AS enrol 
            ON enrol.id = enrolments.enrolid 
        JOIN 
            mo_role_assignments AS roleAssign
            ON roleAssign.userid = teacher.id
        JOIN 
            mo_context AS context
            ON context.id = roleAssign.contextid 
            AND context.contextlevel = 50
        JOIN
            mo_course AS course 
            ON course.id = context.instanceid
            AND enrol.courseid = course.id
        JOIN
            mo_role role 
            ON role.id = roleAssign.roleid 
            AND role.shortname = 'teacher'
        JOIN 
            mo_user_lastaccess AS accessLog 
            ON accessLog.userid = teacher.id 
            AND accessLog.courseid = course.id
        WHERE 
            teacher.suspended = 0
            AND enrol.status = 0
            AND enrolments.status = 0
        HAVING 
            lastAccessInDays > 0
            AND lastAccessInDays < 30
        ORDER BY teacher.id
        LIMIT 20;
        """

    const val GET_ALL_TEACHERS_COURSE_MISSING_WELCOME_MESSAGE = """
        SELECT 
            teacher.id AS userId,
            teacher.firstname,
            teacher.email,
            course.id AS courseId,
            course.shortname AS courseShortname,
            enrolments.status AS courseUserActive,
            teacher.lastaccess AS lastAccess,
            accessLog.timeaccess AS courseLastAccess,
            DATEDIFF(FROM_UNIXTIME(teacher.lastaccess), FROM_UNIXTIME(accessLog.timeaccess)) AS lastAccessInDays
        FROM 
            mo_user AS teacher
        JOIN 
            mo_user_enrolments AS enrolments 
            ON enrolments.userid = teacher.id
        JOIN 
            mo_enrol AS enrol 
            ON enrol.id = enrolments.enrolid 
        JOIN 
            mo_role_assignments AS roleAssign
            ON roleAssign.userid = teacher.id
        JOIN 
            mo_context AS context
            ON context.id = roleAssign.contextid 
            AND context.contextlevel = 50
        JOIN
            mo_course AS course 
            ON course.id = context.instanceid
            AND enrol.courseid = course.id
        JOIN
            mo_role role 
            ON role.id = roleAssign.roleid 
            AND role.shortname = 'teacher'
        JOIN 
            mo_user_lastaccess AS accessLog 
            ON accessLog.userid = teacher.id 
            AND accessLog.courseid = course.id
        WHERE 
            teacher.suspended = 0
            AND enrol.status = 0
            AND enrolments.status = 0
        HAVING 
            lastAccessInDays > 0
            AND lastAccessInDays < 30
        ORDER BY teacher.id
        LIMIT 20;
        """

    const val GET_ALL_TEACHERS_WITH_ASSIGMENTS_PENDING_GRADING = """
        
    """

}