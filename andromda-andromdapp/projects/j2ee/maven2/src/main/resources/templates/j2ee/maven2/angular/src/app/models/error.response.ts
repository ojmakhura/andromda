export class ErrorResponse {
  status: number;
  code: string;
  message: string;
  messageArguments: any[] | null;
  timestamp: string; // ISO 8601 date string (Instant serializes to this via Jackson)

  constructor(
    status: number,
    code: string,
    message: string,
    messageArguments: any[] | null,
    timestamp: string
  ) {
    this.status = status;
    this.code = code;
    this.message = message;
    this.messageArguments = messageArguments;
    this.timestamp = timestamp;
  }
}