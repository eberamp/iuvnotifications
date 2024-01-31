package mx.edu.iuv.monitor.domain.service.output

import mx.edu.iuv.monitor.domain.model.IuvUser

interface TeacherRepository {

    fun getAllInactiveLast24Hours(): List<IuvUser>

}