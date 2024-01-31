package mx.edu.iuv.monitor.infrastructure.output.database

import mx.edu.iuv.monitor.domain.model.IuvUser
import mx.edu.iuv.monitor.domain.service.output.TeacherRepository
import org.springframework.stereotype.Service

@Service
class MySQLTeacherRepository : TeacherRepository {

    override fun getAllInactiveLast24Hours(): List<IuvUser> {
        TODO("Not yet implemented")
    }



}