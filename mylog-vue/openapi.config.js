import { generateService } from '@umijs/openapi'

generateService({
  requestLibPath: "import request from '@/request'",
  schemaPath: 'http://localhost:7333/mylog/v2/api-docs',
  serversPath: './src',
})
