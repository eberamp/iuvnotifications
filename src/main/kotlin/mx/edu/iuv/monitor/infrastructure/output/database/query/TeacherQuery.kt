package mx.edu.iuv.monitor.infrastructure.output.database.query

object TeacherQuery {

    const val GET_ALL_TEACHERS_COURSE_INACTIVE_24_HOURS = """
        SELECT 
            teacher.id AS user_id,
            'teacher' AS user_role,
            teacher.firstname AS first_name,
            teacher.email,
            course.id AS course_id,
            course.shortname AS course_shortname,
            enrolments.status AS user_enrolment_status,
            teacher.lastaccess AS last_platform_access,
            accessLog.timeaccess AS last_course_access,
            TIMESTAMPDIFF(DAY, FROM_UNIXTIME(accessLog.timeaccess), UTC_TIMESTAMP()) AS last_course_access_in_days,
            TIMESTAMPDIFF(DAY, FROM_UNIXTIME(teacher.lastaccess), UTC_TIMESTAMP()) AS last_platform_access_from_current_date_in_days
        FROM 
            mo_user AS teacher
        JOIN 
            mo_user_enrolments AS enrolments 
            ON enrolments.userid = teacher.id
        JOIN 
            mo_enrol AS enrol 
            ON enrol.id = enrolments.enrolid 
        JOIN 
            mo_role_assignments AS roleAssignments
            ON roleAssignments.userid = teacher.id
        JOIN 
            mo_context AS context
            ON context.id = roleAssignments.contextid 
            AND context.contextlevel = 50
        JOIN
            mo_course AS course 
            ON course.id = context.instanceid
            AND enrol.courseid = course.id
        JOIN
            mo_role AS userRole 
            ON userRole.id = roleAssignments.roleid 
            AND userRole.shortname = 'teacher'
        JOIN 
            mo_user_lastaccess AS accessLog 
            ON accessLog.userid = teacher.id 
            AND accessLog.courseid = course.id
        WHERE 
            teacher.suspended = 0 # teacher is not suspended
            AND teacher.deleted = 0 # teacher is not deeleted
            AND enrol.status = 0 # teacher is active
            AND enrolments.status = 0 # enrolment is active
            AND course.visible = 1 # course is visible
            AND teacher.lastaccess > UNIX_TIMESTAMP(NOW() - INTERVAL 20 DAY)
        HAVING
            last_course_access_in_days BETWEEN 1 AND 20
        """

    const val GET_ALL_TEACHERS_COURSE_MISSING_WELCOME_MESSAGE = """            
        SELECT 
            teacher.id AS user_id,
            'teacher' AS user_role,
            teacher.firstname AS first_name,
            teacher.email,
            course.id AS course_id,
            course.shortname AS course_shortname,
            enrolments.status AS user_enrolment_status,
            teacher.lastaccess AS last_platform_access,
            accessLog.timeaccess AS last_course_access,
            TIMESTAMPDIFF(DAY, FROM_UNIXTIME(accessLog.timeaccess), UTC_TIMESTAMP()) AS last_course_access_in_days,
            TIMESTAMPDIFF(DAY, FROM_UNIXTIME(teacher.lastaccess), UTC_TIMESTAMP()) AS last_platform_access_from_current_date_in_days
        FROM 
            mo_user AS teacher
        JOIN 
            mo_user_enrolments AS enrolments 
            ON enrolments.userid = teacher.id
        JOIN 
            mo_enrol AS enrol 
            ON enrol.id = enrolments.enrolid 
        JOIN 
            mo_role_assignments AS roleAssignments
            ON roleAssignments.userid = teacher.id
        JOIN 
            mo_context AS context
            ON context.id = roleAssignments.contextid 
            AND context.contextlevel = 50
        JOIN
            mo_course AS course 
            ON course.id = context.instanceid
            AND enrol.courseid = course.id
        JOIN
            mo_role AS userRole 
            ON userRole.id = roleAssignments.roleid 
            AND userRole.shortname = 'teacher'
        JOIN 
            mo_user_lastaccess AS accessLog 
            ON accessLog.userid = teacher.id 
            AND accessLog.courseid = course.id
        LEFT JOIN
            mo_label AS label
            ON course.id = label.course
        WHERE 
            teacher.suspended = 0
            AND teacher.deleted = 0
            AND enrol.status = 0
            AND enrolments.status = 0
            AND course.visible = 1
            AND (label.id IS NULL OR (label.intro = '' AND label.introformat = 0))
            AND teacher.lastaccess > UNIX_TIMESTAMP(NOW() - INTERVAL 20 DAY)
        HAVING
            last_course_access_in_days BETWEEN 1 AND 20
        """

    const val GET_ALL_TEACHERS_WITH_COURSE_ACTIVITIES_PENDING_GRADING = """
        SELECT 
            teacher.id AS user_id,
            'teacher' AS user_role,
            teacher.firstname AS first_name,
            teacher.email,
            course.id AS course_id,
            course.shortname AS course_shortname,
            enrolments.status AS user_enrolment_status,
            gradeItems.id AS grade_item_id,
            gradeItems.itemname AS grade_item_name,
            student.id AS student_id,
            student.firstname AS student_firstname,
            teacher.lastaccess AS last_platform_access,
            accessLog.timeaccess AS last_course_access,
            TIMESTAMPDIFF(DAY, FROM_UNIXTIME(accessLog.timeaccess), UTC_TIMESTAMP()) AS last_course_access_in_days,
            TIMESTAMPDIFF(DAY, FROM_UNIXTIME(teacher.lastaccess), UTC_TIMESTAMP()) AS last_platform_access_from_current_date_in_days
        FROM 
            mo_user AS teacher
        JOIN 
            mo_user_enrolments AS enrolments 
            ON enrolments.userid = teacher.id
        JOIN 
            mo_enrol AS enrol 
            ON enrol.id = enrolments.enrolid 
        JOIN 
            mo_role_assignments AS roleAssignments
            ON roleAssignments.userid = teacher.id
        JOIN 
            mo_context AS context
            ON context.id = roleAssignments.contextid 
            AND context.contextlevel = 50
        JOIN
            mo_course AS course 
            ON course.id = context.instanceid
            AND enrol.courseid = course.id
        JOIN
            mo_role AS userRole 
            ON userRole.id = roleAssignments.roleid 
            AND userRole.shortname = 'teacher'
        JOIN 
            mo_user_lastaccess AS accessLog 
            ON accessLog.userid = teacher.id 
            AND accessLog.courseid = course.id
        JOIN
            mo_course_modules AS courseModules 
            ON courseModules.course = course.id 
        JOIN mo_grade_items AS gradeItems
            ON gradeItems.iteminstance = courseModules.id
        LEFT JOIN mo_grade_grades grades 
            ON gradeItems.id = grades.itemid
        LEFT JOIN mo_user AS student
            ON grades.userid = student.id
        WHERE 
            teacher.suspended = 0
            AND teacher.deleted = 0
            AND enrol.status = 0
            AND enrolments.status = 0
            AND course.visible = 1
            AND (grades.id IS NULL OR grades.finalgrade IS NULL)
            AND student.firstname IS NOT NULL
            AND teacher.lastaccess > UNIX_TIMESTAMP(NOW() - INTERVAL 20 DAY)
        HAVING
            last_course_access_in_days BETWEEN 1 AND 20
    """

}